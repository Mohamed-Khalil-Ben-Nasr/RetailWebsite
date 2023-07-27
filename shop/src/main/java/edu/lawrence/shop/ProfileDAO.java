package edu.lawrence.shop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ProfileDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Profile findByUser(int user) {
	String sql = "SELECT * FROM profile WHERE user=?";
        RowMapper<Profile> rowMapper = new ProfileRowMapper();
        Profile result = null;
        try {
            result = jdbcTemplate.queryForObject(sql, rowMapper, user);
        } catch (EmptyResultDataAccessException e) {
            
        }
        return result;
    }
    
    public void save(Profile p) {
        String sql = "INSERT INTO profile(user,fullname,phone,email) VALUES (?,?,?,?)";
        jdbcTemplate.update(sql, p.getUser(),p.getFullname(),p.getPhone(),p.getEmail());
    }
    
    public void update(Profile p) {
        String sql = "UPDATE profile SET fullname=?, phone=?, email=? WHERE user=?";
        jdbcTemplate.update(sql, p.getFullname(),p.getPhone(), p.getEmail(),p.getUser());
    }
}
