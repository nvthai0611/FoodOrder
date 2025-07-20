package com.example.foodorder.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Cart implements Serializable {
    private String userId;
    private List<CartItem> cartItems = new ArrayList<>();

    // Hoặc nếu API trả về nested object:
    // private User user;
    // private Food food;

    public Cart() {
    }

    public Cart(String userId, List<CartItem> cartItems) {
        this.userId = userId;
        this.cartItems = cartItems;
    }
    // Getters và Setters


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<CartItem> getCartItems() {
        return this.cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public void setCartItems(CartItem item) {
        this.cartItems.add(item);
    }
}
