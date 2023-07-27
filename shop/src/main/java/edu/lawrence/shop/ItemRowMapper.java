package edu.lawrence.shop;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class ItemRowMapper implements RowMapper<Item> {
    @Override
    public Item mapRow(ResultSet row, int rowNum) throws SQLException {
        Item i = new Item();
        i.setIditem(row.getInt("iditem"));
        i.setName(row.getString("name"));
        i.setDescription(row.getString("description"));
        i.setImage(row.getString("image"));
        i.setPrice(row.getInt("price"));
        i.setStock(row.getInt("stock"));
        i.setSeller(row.getInt("seller"));
        return i;
    }
}