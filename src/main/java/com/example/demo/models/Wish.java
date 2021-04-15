package com.example.demo.models;

public class Wish {

    private int wishID;
    private int userID;
    private String description;
    private String price;
    private boolean isReserved;

    public Wish(int wishID, int userID, String description, String price, boolean isReserved) {
        this.wishID = wishID;
        this.userID = userID;
        this.description = description;
        this.price = price;
        this.isReserved = isReserved;
    }

    public int getWishID() {
        return wishID;
    }

    public int getUserID() {
        return userID;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public boolean isReserved() {
        return isReserved;
    }
}
