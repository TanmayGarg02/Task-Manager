package com.example.taskman.storage;
import org.springframework.web.multipart.MultipartFile;
public interface StorageService {
  String save(String taskId, MultipartFile file);
  byte[] load(String path);
}
