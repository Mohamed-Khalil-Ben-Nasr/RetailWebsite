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
import java.util.List;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/purchase")
@CrossOrigin(origins="*")
public class PurchaseController {
    private PurchaseDAO dao;
    private ItemDAO itemDAO;
    
    public PurchaseController(PurchaseDAO dao,ItemDAO itemDAO) {
        this.dao = dao;
        this.itemDAO = itemDAO;
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
    public ResponseEntity<List<Purchase>> findBySeller(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        String parts[] = auth.split(" ");
        if(parts.length < 2) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        String jwt = parts[1];
        int userId = checkAuth(jwt);
        if(userId < 0) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        List<Purchase> results = dao.findBySeller(userId);
        return ResponseEntity.ok(results);
    }
    
    @PostMapping()
    public ResponseEntity<String> createPurchase(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth,@RequestBody Purchase p) {
        String parts[] = auth.split(" ");
        if(parts.length < 2) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        String jwt = parts[1];
        int userId = checkAuth(jwt);
        if(userId < 0) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authorized");
        }
        p.setBuyer(userId);
        dao.save(p);
        return ResponseEntity.ok("Purchase created");
    }
    
    @GetMapping("/ship/{id}")
    public ResponseEntity<String> shipItem(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth,@PathVariable("id") int id) {
        String parts[] = auth.split(" ");
        if(parts.length < 2) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        String jwt = parts[1];
        int userId = checkAuth(jwt);
        if(userId < 0) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authorized");
        }
        Purchase p = dao.findById(id);
        Item i = itemDAO.findById(p.getItem());
        if(i.getSeller() != userId){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authorized");
        }
        itemDAO.decreaseStock(i.getIditem());
        dao.remove(id);
        
        return ResponseEntity.ok("Item shipped");
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
        Purchase p = dao.findById(id);
        Item i = itemDAO.findById(p.getItem());
        if(i.getSeller() != userId){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authorized");
        }
        dao.remove(id);
        return ResponseEntity.ok("Purchase removed");
    }

}
