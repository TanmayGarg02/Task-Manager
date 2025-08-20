package com.example.taskman.user;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity @Table(name="users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
  @Column(unique = true, nullable = false) String email;
  @Column(nullable = false) String passwordHash;
  @Enumerated(EnumType.STRING) Role role = Role.USER;
  Instant createdAt = Instant.now();
}
