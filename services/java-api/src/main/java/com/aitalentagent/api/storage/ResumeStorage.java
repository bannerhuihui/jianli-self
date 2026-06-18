package com.aitalentagent.api.storage;

import org.springframework.web.multipart.MultipartFile;

public interface ResumeStorage {

  /**
   * 持久化简历源文件，返回写入 {@code resume_files.storage_path} 的值。
   * 本地模式为绝对路径；OSS 代理模式为可公网 GET 的 URL。
   */
  String store(String journeyId, String fileId, String extension, MultipartFile file);
}
