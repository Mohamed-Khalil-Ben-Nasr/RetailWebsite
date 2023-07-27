package edu.lawrence.shop;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins="*")
public class UserController {
    private UserDAO dao;
    
    public UserController(UserDAO dao) {
        this.dao = dao;
    }
    
    private String makeJWT(long id,String name) {
        String secret = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";
        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret), 
                                    SignatureAlgorithm.HS256.getJcaName());
        Instant now = Instant.now();
        String jwtToken = Jwts.builder()
        .claim("name", name)
        .claim("id", Long.toString(id))
        .setSubject("user")
        .setId(UUID.randomUUID().toString())
        .setIssuedAt(Date.from(now))
        .setExpiration(Date.from(now.plus(60l, ChronoUnit.MINUTES)))
        .signWith(hmacKey)
        .compact();
        return jwtToken;
    }
    
    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody User u) {
        User existing = dao.findByName(u.getName());
        if(existing != null) {
            return ResponseEntity.badRequest()
            .body("User "+u.getName()+" already exists.");
        }
        long id = dao.save(u);
        String jwtToken = makeJWT(id,u.getName());
        return ResponseEntity.status(HttpStatus.OK).body(jwtToken);
    }
    
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody User u) {
        User existing = dao.findByName(u.getName());
        if(existing == null || !u.getPassword().equals(existing.getPassword())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body("Login failed");
        }
        long id = existing.getId();
        String jwtToken = makeJWT(id,u.getName());
        return ResponseEntity.status(HttpStatus.OK).body(jwtToken);
    }

}
