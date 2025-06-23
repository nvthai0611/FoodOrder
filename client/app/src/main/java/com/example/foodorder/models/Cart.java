package com.example.foodorder.models;

public class Cart {
    private int id;
    private int userId;
    private int foodId;
    private int quantity;

    // Hoặc nếu API trả về nested object:
    // private User user;
    // private Food food;

    public Cart() {}

    public Cart(int id, int userId, int foodId, int quantity) {
        this.id = id;
        this.userId = userId;
        this.foodId = foodId;
        this.quantity = quantity;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
