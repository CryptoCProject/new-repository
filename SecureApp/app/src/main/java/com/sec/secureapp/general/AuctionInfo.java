package com.sec.secureapp.general;

public class AuctionInfo {

    private String auction_type;
    private String auctioneer_id;
    private String object_name;
    private double initial_price;

    public AuctionInfo(String auction_type, String auctioneer_id, String object_name, double initial_price) {
        this.auction_type = auction_type;
        this.auctioneer_id = auctioneer_id;
        this.object_name = object_name;
        this.initial_price = initial_price;
    }

    public String getAuction_type() {
        return auction_type;
    }

    public void setAuction_type(String auction_type) {
        this.auction_type = auction_type;
    }

    public String getAuctioneer_id() {
        return auctioneer_id;
    }

    public void setAuctioneer_id(String auctioneer_id) {
        this.auctioneer_id = auctioneer_id;
    }

    public String getObject_name() {
        return object_name;
    }

    public void setObject_name(String object_name) {
        this.object_name = object_name;
    }

    public double getInitial_price() {
        return initial_price;
    }

    public void setInitial_price(double initial_price) {
        this.initial_price = initial_price;
    }
}
