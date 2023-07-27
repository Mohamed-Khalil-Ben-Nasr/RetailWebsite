package edu.lawrence.shop;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.util.Base64;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/profile")
@CrossOrigin(origins="*")
public class ProfileController {
    private ProfileDAO dao;
    
    public ProfileController(ProfileDAO dao) {
        this.dao = dao;
    }
    
    private int checkAuth(String auth) {
        String secret = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";
        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret), 
                                        SignatureAlgorithm.HS256.getJcaName());
        int result = -1;
    
        try {
            Jws<Claims> jwt = Jwts.parserBuilder()
                    .setSigningKey(hmacKey)
                    .build()
                    .parseClaimsJws(auth);
            result = Integer.parseInt(jwt.getBody().get("id", String.class));
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
@GetMapping()
public ResponseEntity<Profile> getProfile(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
String parts[] = auth.split(" ");
if(parts.length < 2) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
String jwt = parts[1];
int userId = checkAuth(jwt);
if(userId < 0) {
return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
}
Profile p = dao.findByUser(userId);
if(p == null)
return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
return ResponseEntity.ok(p);
}

    
    @PostMapping("/create")
    public ResponseEntity<String> createProfile(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth,@RequestBody Profile p) {
        String parts[] = auth.split(" ");
        if(parts.length < 2) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        String jwt = parts[1];
        int userId = checkAuth(jwt);
        if(userId < 0) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authorized");
        }
        p.setUser(userId);
        dao.save(p);
        return ResponseEntity.ok("Profile created");
    }
    
    @PostMapping("/update")
    public ResponseEntity<String> updateProfile(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth,@RequestBody Profile p) {
        String parts[] = auth.split(" ");
        if(parts.length < 2) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        String jwt = parts[1];
        int userId = checkAuth(jwt);
        if(userId < 0) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authorized");
        }
        p.setUser(userId);
        dao.update(p);
        return ResponseEntity.ok("Profile updated");
    }

}
