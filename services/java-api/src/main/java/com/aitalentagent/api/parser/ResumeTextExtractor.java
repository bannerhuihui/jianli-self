package com.aitalentagent.api.parser;

import com.aitalentagent.api.common.ApiException;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

@Component
public class ResumeTextExtractor {

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(30))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();

    public String extract(String storagePath, String fileType) {
        try (InputStream inputStream = openStream(storagePath)) {
            return extract(inputStream, fileType);
        } catch (ApiException ex) {
            throw ex;
        } catch (IOException ex) {
            throw new ApiException("RESUME_PARSE_FAILED", "读取简历文件失败", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public String extract(InputStream inputStream, String fileType) throws IOException {
        return switch (fileType.toLowerCase()) {
            case "pdf" -> extractPdf(inputStream);
            case "docx" -> extractDocx(inputStream);
            case "doc" -> throw new ApiException(
                    "RESUME_FILE_TYPE_UNSUPPORTED",
                    "暂不支持 .doc 格式，请上传 .docx 或 .pdf",
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE
            );
            default -> throw new ApiException(
                    "RESUME_FILE_TYPE_UNSUPPORTED",
                    "不支持的文件类型: " + fileType,
                    HttpStatus.UNSUPPORTED_MEDIA_TYPE
            );
        };
    }

    private InputStream openStream(String storagePath) throws IOException {
        if (isRemoteUrl(storagePath)) {
            return download(storagePath);
        }
        return Files.newInputStream(Path.of(storagePath));
    }

    private boolean isRemoteUrl(String storagePath) {
        return storagePath.startsWith("http://") || storagePath.startsWith("https://");
    }

    private InputStream download(String url) throws IOException {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(60))
                    .GET()
                    .build();
            HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new ApiException(
                        "RESUME_PARSE_FAILED",
                        "下载简历文件失败: HTTP " + response.statusCode(),
                        HttpStatus.UNPROCESSABLE_ENTITY
                );
            }
            return new ByteArrayInputStream(response.body());
        } catch (ApiException ex) {
            throw ex;
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new ApiException("RESUME_PARSE_FAILED", "下载简历文件被中断", HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception ex) {
            throw new ApiException("RESUME_PARSE_FAILED", "下载简历文件失败", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    private String extractPdf(InputStream inputStream) throws IOException {
        try (PDDocument document = Loader.loadPDF(inputStream.readAllBytes())) {
            PDFTextStripper stripper = new PDFTextStripper();
            return normalize(stripper.getText(document));
        }
    }

    private String extractDocx(InputStream inputStream) throws IOException {
        try (XWPFDocument document = new XWPFDocument(inputStream);
             XWPFWordExtractor extractor = new XWPFWordExtractor(document)) {
            return normalize(extractor.getText());
        }
    }

    private String normalize(String text) {
        if (text == null) {
            return "";
        }
        return text.replace('\u0000', ' ').trim();
    }
}
