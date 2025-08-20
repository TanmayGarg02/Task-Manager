package com.example.taskman.task;
import jakarta.persistence.*;
import lombok.*;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Attachment {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
  String filename;
  String contentType;
  long sizeBytes;
  String url;
}
