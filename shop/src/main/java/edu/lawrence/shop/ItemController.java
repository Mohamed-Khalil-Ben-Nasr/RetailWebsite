package edu.lawrence.shop;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.util.Base64;
import java.util.List;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/item")
@CrossOrigin(origins="*")
public class ItemController {
    private ItemDAO dao;
    
    public ItemController(ItemDAO dao) {
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
    
    @GetMapping("/forseller")
    public ResponseEntity<List<Item>> findBySeller(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        String parts[] = auth.split(" ");
        if(parts.length < 2) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        String jwt = parts[1];
        int userId = checkAuth(jwt);
        if(userId < 0) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        List<Item> results = dao.findBySeller(userId);
        return ResponseEntity.ok(results);
    }
    
    @GetMapping(params={"tag"})
    public List<Item> findByTag(@RequestParam String tag) {
        return dao.findByTag(tag);
    }
    
    @GetMapping("/all")
    public List<Item> findAll() {
        return dao.findAll();
    }
    
    @GetMapping("/{id}")
    public Item findById(@PathVariable("id") int id) {
        return dao.findById(id);
    }
    
    @PostMapping()
    public ResponseEntity<String> addItem(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth,@RequestBody Item i) {
        String parts[] = auth.split(" ");
        if(parts.length < 2) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        String jwt = parts[1];
        int userId = checkAuth(jwt);
        if(userId < 0) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authorized");
        }
        i.setSeller(userId);
        dao.save(i);
        return ResponseEntity.ok("Item created");
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeItem(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth,@PathVariable("id") int id) {
        String parts[] = auth.split(" ");
        if(parts.length < 2) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        String jwt = parts[1];
        int userId = checkAuth(jwt);
        if(userId < 0) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authorized");
        }
        dao.remove(id);
        return ResponseEntity.ok("Item removed");
    }
}
