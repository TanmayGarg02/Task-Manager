package com.example.taskman.security;
import io.jsonwebtoken.*; import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value; import org.springframework.stereotype.Service;
import java.security.Key; import java.util.Date; import java.util.Map;

@Service
public class JwtService {
  private final Key key; private final long expiresMs;
  public JwtService(@Value("${app.jwt.secret}") String secret,
                    @Value("${app.jwt.expires-in}") long expiresSec){
    this.key = Keys.hmacShaKeyFor(secret.getBytes());
    this.expiresMs = expiresSec * 1000;
  }
  public String createToken(String sub, Map<String,Object> claims){
    long now = System.currentTimeMillis();
    return Jwts.builder().setSubject(sub).addClaims(claims)
      .setIssuedAt(new Date(now)).setExpiration(new Date(now + expiresMs))
      .signWith(key, SignatureAlgorithm.HS256).compact();
  }
  public Jws<Claims> parse(String token){
    return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
  }
}
