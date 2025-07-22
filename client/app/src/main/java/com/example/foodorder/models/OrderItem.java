package com.example.foodorder.models;

import java.io.Serializable;

public class OrderItem {
    private String _id;
    private Food food_id;
    private int quantity;
    private int price;

    public OrderItem() {
    }

    public OrderItem(Food food_id, String _id, int quantity, int price) {
        this.food_id = food_id;
        this._id = _id;
        this.quantity = quantity;
        this.price = price;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Food getFood_id() {
        return food_id;
    }

    public void setFood_id(Food food_id) {
        this.food_id = food_id;
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
}

