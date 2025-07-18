package com.example.foodorder.models;

public class CartItem {
    public Integer id;
    public String name;
    public Double price;
    public Integer quantity;
    public String image;

    public CartItem(Integer id, String name, double price, int quantity, String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
    }

    public CartItem(){

    }
}
