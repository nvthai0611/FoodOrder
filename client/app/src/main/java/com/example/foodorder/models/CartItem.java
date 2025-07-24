package com.example.foodorder.models;

import com.example.foodorder.activity.FoodActivity;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CartItem implements Serializable {
    @SerializedName("food")
    private String foodId;
    private String name;
    private Double price;
    private Integer quantity;

    public String getFoodId() {
        return foodId;
    }

    private String image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public CartItem(String foodId, String name, double price, int quantity, String image) {
        this.foodId = foodId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
    }

    public CartItem() {

    }
}
