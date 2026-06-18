package com.aitalentagent.api.storage;

import com.aitalentagent.api.common.ApiException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
@ConditionalOnProperty(name = "app.storage.type", havingValue = "local", matchIfMissing = true)
public class LocalResumeStorage implements ResumeStorage {

  private final Path uploadDir;

  public LocalResumeStorage() {
    this.uploadDir = Path.of(System.getProperty("java.io.tmpdir"), "ai-talent-agent-uploads");
  }

  @Override
  public String store(String journeyId, String fileId, String extension, MultipartFile file) {
    try {
      Files.createDirectories(uploadDir);
      Path target = uploadDir.resolve(fileId + "." + extension);
      file.transferTo(target);
      return target.toString();
    } catch (IOException ex) {
      throw new ApiException("INTERNAL_ERROR", "文件保存失败", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
