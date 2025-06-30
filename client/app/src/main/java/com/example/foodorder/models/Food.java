package com.example.foodorder.models;


public class Food {
    private int id;
    private String name;
    private String description;
    private String imageUrl;
    private Category category;
    private double price;
    private boolean isAvailable;
    private String createdAt;
    private Boolean isBestSeller;
    private int rating; // Thêm trường Rating
    public Food() {}

    public Food(int id, String name, String description, String imageUrl, Category category, double price, boolean isAvailable, String createdAt, Boolean isBestSeller, int rating) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.category = category;
        this.price = price;
        this.isAvailable = isAvailable;
        this.createdAt = createdAt;
        this.isBestSeller = isBestSeller;
        this.rating = rating;
    }

    public Food(int id, String name, String description, String imageUrl, Category category, double price, boolean isAvailable, String createdAt, int Rating) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.category = category;
        this.price = price;
        this.isAvailable = isAvailable;
        this.createdAt = createdAt;
        this.rating = 0; // Khởi tạo Rating mặc định là 0
    }

    // Getters và Setters


    public Boolean getBestSeller() {
        return isBestSeller;
    }

    public void setBestSeller(Boolean bestSeller) {
        isBestSeller = bestSeller;
    }

    public void setRating(int rating) {
        rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getRating() {
        return rating;
    }
}


