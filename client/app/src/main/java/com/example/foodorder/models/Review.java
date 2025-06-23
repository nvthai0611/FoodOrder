package com.example.foodorder.models;

public class Review {
    private int id;
    private int userId;
    private int foodId;
    private int rating;      // từ 1 đến 5
    private String comment;
    private String createdAt;

    public Review() {}

    public Review(int id, int userId, int foodId, int rating, String comment, String createdAt) {
        this.id = id;
        this.userId = userId;
        this.foodId = foodId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    // Getters và Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
