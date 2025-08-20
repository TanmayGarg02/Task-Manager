package com.example.taskman.auth;
import com.example.taskman.security.JwtService;
import com.example.taskman.user.*;
import jakarta.validation.constraints.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

record RegisterReq(@Email String email, @Size(min=6) String password){}
record LoginReq(@Email String email, @NotBlank String password){}
record AuthRes(String token, Long userId, Role role, String email){}

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final UserRepo repo;
  private final PasswordEncoder encoder;
  private final JwtService jwt;
  public AuthController(UserRepo r, PasswordEncoder e, JwtService j){ repo=r; encoder=e; jwt=j; }

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody RegisterReq req){
    if (repo.findByEmail(req.email()).isPresent()) return ResponseEntity.badRequest().body(Map.of("message","Email already exists"));
    var isFirst = repo.count()==0;
    var user = User.builder().email(req.email()).passwordHash(encoder.encode(req.password()))
      .role(isFirst? Role.ADMIN:Role.USER).build();
    repo.save(user);
    var token = jwt.createToken(user.getEmail(), Map.of("role", user.getRole().name()));
    return ResponseEntity.ok(new AuthRes(token, user.getId(), user.getRole(), user.getEmail()));
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginReq req){
    var user = repo.findByEmail(req.email()).orElse(null);
    if (user==null || !encoder.matches(req.password(), user.getPasswordHash()))
      return ResponseEntity.status(401).body(Map.of("message","Invalid credentials"));
    var token = jwt.createToken(user.getEmail(), Map.of("role", user.getRole().name()));
    return ResponseEntity.ok(new AuthRes(token, user.getId(), user.getRole(), user.getEmail()));
  }
}
