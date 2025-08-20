package com.example.taskman.storage;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/files")
public class FileController {
  private final LocalStorageService local;
  public FileController(LocalStorageService s){ this.local = s; }

  @GetMapping("/{taskId}/{filename}")
  public ResponseEntity<byte[]> download(@PathVariable String taskId, @PathVariable String filename){
    var data = local.load(taskId + "/" + filename);
    return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(data);
  }
}
