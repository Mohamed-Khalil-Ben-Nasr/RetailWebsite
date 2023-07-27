package edu.lawrence.shop;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class PurchaseDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Purchase> findBySeller(int seller) {
	String sql = "SELECT idpurchase, item, buyer, time FROM item JOIN purchase ON purchase.item=item.iditem WHERE item.seller=?";
        RowMapper<Purchase> rowMapper = new PurchaseRowMapper();
        return jdbcTemplate.query(sql, rowMapper, seller);
    }
    
    public Purchase findById(int id) {
        String sql = "SELECT * FROM purchase WHERE idpurchase=?";
        RowMapper<Purchase> rowMapper = new PurchaseRowMapper();
        Purchase result = null;
        try {
            result = jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            
        }
        return result;
    }
    
    public void save(Purchase p) {
        String sql = "INSERT INTO purchase(item,buyer) VALUES (?,?)";
        jdbcTemplate.update(sql, p.getItem(),p.getBuyer());
    }
    
    public void remove(int id) {
        String sql = "DELETE FROM purchase WHERE idpurchase=?";
        jdbcTemplate.update(sql,id);
    }
}