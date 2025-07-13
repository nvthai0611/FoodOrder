package com.example.foodorder.models;

import java.io.Serializable;

public class OrderItem implements Serializable {
    public String food_id;
    public String name;
    public int quantity;
    public int price;

    private String image_url;

    public OrderItem() {
    }

    public OrderItem(String food_id, String name, int quantity, int price) {
        this.food_id = food_id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public String getFood_id() {
        return food_id;
    }

    public void setFood_id(String food_id) {
        this.food_id = food_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
