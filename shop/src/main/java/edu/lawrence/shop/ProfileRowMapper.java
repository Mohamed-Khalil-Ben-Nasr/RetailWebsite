package edu.lawrence.shop;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class ProfileRowMapper implements RowMapper<Profile> {
    @Override
    public Profile mapRow(ResultSet row, int rowNum) throws SQLException {
        Profile p = new Profile();
        p.setUser(row.getInt("user"));
        p.setFullname(row.getString("fullname"));
        p.setPhone(row.getString("phone"));
        p.setEmail(row.getString("email"));
        return p;
    }
}
