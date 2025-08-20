package com.example.taskman.task;
import com.example.taskman.user.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.*;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Table(indexes = { @Index(columnList = "status"), @Index(columnList = "priority"), @Index(columnList = "dueDate") })
public class Task {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
  String title;
  @Column(length=4000) String description;
  @Enumerated(EnumType.STRING) TaskStatus status = TaskStatus.TODO;
  @Enumerated(EnumType.STRING) TaskPriority priority = TaskPriority.MEDIUM;
  LocalDate dueDate;
  @ManyToOne(fetch = FetchType.LAZY) User assignedTo;
  @ManyToOne(fetch = FetchType.LAZY) User createdBy;
  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true) @JoinColumn(name="task_id")
  List<Attachment> attachments = new ArrayList<>();
}
