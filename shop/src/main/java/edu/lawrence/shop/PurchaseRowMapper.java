package edu.lawrence.shop;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class PurchaseRowMapper implements RowMapper<Purchase> {
    @Override
    public Purchase mapRow(ResultSet row, int rowNum) throws SQLException {
        Purchase p = new Purchase();
        p.setIdpurchase(row.getInt("idpurchase"));
        p.setItem(row.getInt("item"));
        p.setBuyer(row.getInt("buyer"));
        p.setTime(row.getDate("time"));
        return p;
    }
}