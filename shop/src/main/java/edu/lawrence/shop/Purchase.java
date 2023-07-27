package edu.lawrence.shop;

import java.sql.Date;

public class Purchase {
    private int idpurchase;
    private int item;
    private int buyer;
    private Date time;
    
    public Purchase() {}

    public int getIdpurchase() {
        return idpurchase;
    }

    public void setIdpurchase(int idpurchase) {
        this.idpurchase = idpurchase;
    }

    public int getItem() {
        return item;
    }

    public void setItem(int item) {
        this.item = item;
    }

    public int getBuyer() {
        return buyer;
    }

    public void setBuyer(int buyer) {
        this.buyer = buyer;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
    
}
