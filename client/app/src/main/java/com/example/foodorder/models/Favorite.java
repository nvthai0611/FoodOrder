package com.example.foodorder.models;

public class Favorite {
    private int id;
    private int userId;
    private int foodId;
    private String note;
    private String createdAt;

    public Favorite() {}

    public Favorite(int id, int userId, int foodId, String note, String createdAt) {
        this.id = id;
        this.userId = userId;
        this.foodId = foodId;
        this.note = note;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
