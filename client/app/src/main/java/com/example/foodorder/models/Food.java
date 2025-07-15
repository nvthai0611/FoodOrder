package com.example.foodorder.models;


import java.io.Serializable;

public class Food implements Serializable {
    private String _id;
    private String name;
    private String description;
    private String image_url;
    private Category category;
    private double price;
    private boolean isBestSeller;
    private double rating;
    private boolean is_available;
    private String created_at;

    public Food() {
    }

    public Food(String _id, String name, String description, String image_url, double price, double rating) {
        this._id = _id;
        this.name = name;
        this.description = description;
        this.image_url = image_url;
        this.price = price;
        this.rating = rating;
    }

    public Food(String _id, String name, String description, String image_url, Category category, double price, boolean isBestSeller, double rating, boolean is_available, String created_at) {
        this._id = _id;
        this.name = name;
        this.description = description;
        this.image_url = image_url;
        this.category = category;
        this.price = price;
        this.isBestSeller = isBestSeller;
        this.rating = rating;
        this.is_available = is_available;
        this.created_at = created_at;
    }

    // Getters & Setters
    public String getId() { return _id; }
    public void setId(String _id) { this._id = _id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return image_url; }
    public void setImageUrl(String image_url) { this.image_url = image_url; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public boolean isBestSeller() { return isBestSeller; }
    public void setBestSeller(boolean bestSeller) { isBestSeller = bestSeller; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public boolean isAvailable() { return is_available; }
    public void setAvailable(boolean is_available) { this.is_available = is_available; }

    public String getCreatedAt() { return created_at; }
    public void setCreatedAt(String created_at) { this.created_at = created_at; }
}

