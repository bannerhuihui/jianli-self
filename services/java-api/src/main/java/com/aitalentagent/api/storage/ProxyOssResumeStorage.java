package com.aitalentagent.api.storage;

import com.aitalentagent.api.common.ApiException;
import com.aitalentagent.api.config.AppProperties;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
@ConditionalOnProperty(name = "app.storage.type", havingValue = "proxy-oss")
public class ProxyOssResumeStorage implements ResumeStorage {

  private static final int SUCCESS_CODE = 2000;

  private final AppProperties appProperties;
  private final RestClient restClient;

  public ProxyOssResumeStorage(AppProperties appProperties) {
    this.appProperties = appProperties;
    this.restClient = RestClient.create();
  }

  @Override
  public String store(String journeyId, String fileId, String extension, MultipartFile file) {
    AppProperties.ProxyOss proxyOss = appProperties.getStorage().getProxyOss();
    String uploadUrl = proxyOss.getUploadUrl();
    if (!StringUtils.hasText(uploadUrl)) {
      throw new ApiException("INTERNAL_ERROR", "未配置 OSS 上传地址", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    String prefix = buildPrefix(proxyOss.getPrefix(), journeyId);
    String filename = resolveFilename(file, fileId, extension);

    try {
      byte[] bytes = file.getBytes();
      MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
      bodyBuilder.part("prefix", prefix);
      bodyBuilder.part("file", new ByteArrayResource(bytes) {
        @Override
        public String getFilename() {
          return filename;
        }
      }).contentType(resolveContentType(file));

      JsonNode response = restClient.post()
          .uri(uploadUrl)
          .body(bodyBuilder.build())
          .retrieve()
          .body(JsonNode.class);

      if (response == null) {
        throw new ApiException("INTERNAL_ERROR", "OSS 上传无响应", HttpStatus.INTERNAL_SERVER_ERROR);
      }

      int code = response.path("code").asInt(-1);
      if (code != SUCCESS_CODE) {
        String message = response.path("message").asText("OSS 上传失败");
        throw new ApiException("INTERNAL_ERROR", message, HttpStatus.INTERNAL_SERVER_ERROR);
      }

      String url = response.path("data").path("url").asText("");
      if (!StringUtils.hasText(url)) {
        throw new ApiException("INTERNAL_ERROR", "OSS 上传未返回文件 URL", HttpStatus.INTERNAL_SERVER_ERROR);
      }
      return url;
    } catch (IOException ex) {
      throw new ApiException("INTERNAL_ERROR", "读取上传文件失败", HttpStatus.INTERNAL_SERVER_ERROR);
    } catch (RestClientException ex) {
      throw new ApiException("INTERNAL_ERROR", "OSS 上传请求失败: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private MediaType resolveContentType(MultipartFile file) {
    if (StringUtils.hasText(file.getContentType())) {
      return MediaType.parseMediaType(file.getContentType());
    }
    return MediaType.APPLICATION_OCTET_STREAM;
  }

  private String buildPrefix(String configuredPrefix, String journeyId) {
    String base = StringUtils.hasText(configuredPrefix) ? configuredPrefix : "jianli/resume";
    return base + "/" + journeyId;
  }

  private String resolveFilename(MultipartFile file, String fileId, String extension) {
    if (StringUtils.hasText(file.getOriginalFilename())) {
      return file.getOriginalFilename();
    }
    return fileId + "." + extension;
  }
}
