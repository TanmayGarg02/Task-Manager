package com.example.taskman.storage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*; import java.nio.file.*; 

@Service
public class LocalStorageService implements StorageService {
  private final Path root;
  public LocalStorageService(@Value("${app.storage.local-dir}") String dir){ this.root = Path.of(dir); }

  @Override public String save(String taskId, MultipartFile file){
    try {
      Path folder = root.resolve(taskId); Files.createDirectories(folder);
      String safe = file.getOriginalFilename().replaceAll("[^a-zA-Z0-9._-]","_");
      Path dest = folder.resolve(safe);
      try (var in = file.getInputStream()) { Files.copy(in, dest, StandardCopyOption.REPLACE_EXISTING); }
      return "/api/files/" + taskId + "/" + dest.getFileName();
    } catch (IOException e){ throw new RuntimeException(e); }
  }

  @Override public byte[] load(String path){
    try { return Files.readAllBytes(root.resolve(path)); }
    catch (IOException e){ throw new RuntimeException(e); }
  }
}
