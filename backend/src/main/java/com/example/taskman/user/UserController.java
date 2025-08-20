package com.example.taskman.user;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/users") @PreAuthorize("hasRole('ADMIN')")
public class UserController {
  private final UserRepo repo;
  public UserController(UserRepo r){ this.repo = r; }

  @GetMapping
  public Page<User> list(@RequestParam(defaultValue="0") int page,
                         @RequestParam(defaultValue="10") int size){
    return repo.findAll(PageRequest.of(page, size, Sort.by("id").descending()));
  }

  @DeleteMapping("/{id}") public void delete(@PathVariable Long id){ repo.deleteById(id); }
}
