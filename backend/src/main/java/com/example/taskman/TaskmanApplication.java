package com.example.taskman;
import com.example.taskman.user.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class TaskmanApplication {
  public static void main(String[] args) { SpringApplication.run(TaskmanApplication.class, args); }

  @Bean CommandLineRunner seed(UserRepo repo, PasswordEncoder pe){
    return args -> {
      if (repo.count()==0){
        repo.save(User.builder().email("admin@local")
          .passwordHash(pe.encode("admin123")).role(Role.ADMIN).build());
      }
    };
  }
}
