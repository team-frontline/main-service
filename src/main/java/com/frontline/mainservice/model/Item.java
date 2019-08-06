package com.frontline.mainservice.model;

public class Item {
    private String itemID;
    private String itemName;
    private int quantity;
    private double price;
    private Integer numberOfRaters;
    private Integer rating;

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setNumberOfRaters(Integer numberOfRaters) {
        this.numberOfRaters = numberOfRaters;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getItemID() {
        return itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public Integer getNumberOfRaters() {
        return numberOfRaters;
    }

    public Integer getRating() {
        return rating;
    }
}
