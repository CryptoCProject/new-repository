package auctioneum.smartcontracts;

public class Auction {
    
    private int auction_id;
    private String auctioneer_id;
    private String auction_type;
    private double object_price;
    private String object_name;

    public Auction(int auction_id, String auctioneer_id, String auction_type, double object_price, String object_name) {
        this.auction_id = auction_id;
        this.auctioneer_id = auctioneer_id;
        this.auction_type = auction_type;
        this.object_price = object_price;
        this.object_name = object_name;
    }

    public int getAuction_id() {
        return auction_id;
    }

    public void setAuction_id(int auction_id) {
        this.auction_id = auction_id;
    }

    public String getAuctioneer_id() {
        return auctioneer_id;
    }

    public void setAuctioneer_id(String auctioneer_id) {
        this.auctioneer_id = auctioneer_id;
    }

    public String getAuction_type() {
        return auction_type;
    }

    public void setAuction_type(String auction_type) {
        this.auction_type = auction_type;
    }

    public double getObject_price() {
        return object_price;
    }

    public void setObject_price(double object_price) {
        this.object_price = object_price;
    }

    public String getObject_name() {
        return object_name;
    }

    public void setObject_name(String object_name) {
        this.object_name = object_name;
    }

    
}
