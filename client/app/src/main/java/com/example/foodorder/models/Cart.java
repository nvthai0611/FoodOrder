package com.example.foodorder.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Cart implements Serializable {
    private int id;
    private int userId;
    private List<CartItem> cartItems = new ArrayList<>();

    // Hoặc nếu API trả về nested object:
    // private User user;
    // private Food food;

    public Cart() {
    }

    public Cart(int id, int userId, List<CartItem> cartItems) {
        this.id = id;
        this.userId = userId;
        this.cartItems = cartItems;
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

    public List<CartItem> getCartItems() {
        return this.cartItems;
    }

    public void setCartItems(List<CartItem> cartItems){
        this.cartItems = cartItems;
    }

    public CartItem getCartItemByItemId(int id){
        if(this.cartItems.isEmpty()) return new CartItem();

        final CartItem[] result = {new CartItem()};
        this.cartItems.forEach(cart -> {
            if(cart.id == id) result[0] = cart;
        });

        return result[0];
    }
}
