package edu.lawrence.shop;

import java.sql.PreparedStatement;
import java.sql.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public User findByName(String name) {
	String sql = "SELECT * FROM user WHERE name=?";
        RowMapper<User> rowMapper = new UserRowMapper();
        User result = null;
        try {
            result = jdbcTemplate.queryForObject(sql, rowMapper, name);
        } catch (EmptyResultDataAccessException e) {
            
        }
        return result;
    }
    
    public long save(User u) {
        String insertSQL = "INSERT INTO user (name,password) values (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
              PreparedStatement ps = connection
              .prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
              ps.setString(1, u.getName());
              ps.setString(2,u.getPassword());
              return ps;
            }, keyHolder);

        return keyHolder.getKey().longValue();
    }
}
