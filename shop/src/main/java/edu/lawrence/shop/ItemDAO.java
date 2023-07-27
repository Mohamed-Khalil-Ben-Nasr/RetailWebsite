package edu.lawrence.shop;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ItemDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Item> findByTag(String tag) {
	String tagsSQL = "SELECT item FROM tag WHERE tag=\'"+tag+"\'";
        String itemSQL = "SELECT * from item WHERE iditem=?";
        List<Integer> codes = jdbcTemplate.queryForList(tagsSQL,Integer.class);
        List<Item> items = new ArrayList<Item>();
        ItemRowMapper rowMapper = new ItemRowMapper();
        for(Integer i : codes) {
            Item result = null;
            try {
                result = jdbcTemplate.queryForObject(itemSQL, rowMapper, i.intValue());
                items.add(result);
            } catch (EmptyResultDataAccessException e) {

            }
        }
        
        return items;
    }
    
    public List<Item> findBySeller(int id) {
	String sql = "SELECT * from item WHERE seller=?";
        ItemRowMapper rowMapper = new ItemRowMapper();
        return jdbcTemplate.query(sql,rowMapper,id);
    }
    
    public List<Item> findAll() {
	String sql = "SELECT * from item";
        ItemRowMapper rowMapper = new ItemRowMapper();
        return jdbcTemplate.query(sql,rowMapper);
    }
    
    public Item findById(int id) {
        String sql = "SELECT * FROM item WHERE iditem=?";
        RowMapper<Item> rowMapper = new ItemRowMapper();
        Item result = null;
        try {
            result = jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            
        }
        return result;
    }
    
    public void save(Item i) {
        String itemSQL = "INSERT INTO item(name,description,image,price,stock,seller) VALUES (?,?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
              PreparedStatement ps = connection
              .prepareStatement(itemSQL, Statement.RETURN_GENERATED_KEYS);
              ps.setString(1, i.getName());
              ps.setString(2,i.getDescription());
              ps.setString(3, i.getImage());
              ps.setInt(4, i.getPrice());
              ps.setInt(5, i.getStock());
              ps.setInt(6, i.getSeller());
              return ps;
            }, keyHolder);

        long id =  keyHolder.getKey().longValue();
        
        for(String tag : i.getTags()) {
            String tagSQL = "INSERT INTO tag(item,tag) VALUES (?,?)";
            jdbcTemplate.update(tagSQL, id,tag);
        }
    }
    
    public void decreaseStock(int item) {
        String updateSQL = "UPDATE item SET stock=stock-1 WHERE iditem=?";
        jdbcTemplate.update(updateSQL,item);
    }
    
    public void increaseStock(int item,int qty) {
        String updateSQL = "UPDATE item SET stock=stock+? WHERE iditem=?";
        jdbcTemplate.update(updateSQL,qty,item);
    }
    
    public void remove(int item) {
        String itemSQL = "DELETE FROM item WHERE iditem=?";
        jdbcTemplate.update(itemSQL,item);
        String tagSQL = "DELETE FROM tag WHERE item=?";
        jdbcTemplate.update(tagSQL,item);
    }
}