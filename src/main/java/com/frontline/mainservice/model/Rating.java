package com.frontline.mainservice.model;

public class Rating {
    private Integer ratingId;
    private String productId;
    private Integer rating;
    private Integer numberOfRaters = 1;

    public Integer getRatingId() {
        return ratingId;
    }

    public void setRatingId(Integer ratingId) {
        this.ratingId = ratingId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getNumberOfRaters() {
        return numberOfRaters;
    }

    public void setNumberOfRaters(Integer numberOfRaters) {
        this.numberOfRaters = numberOfRaters;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
