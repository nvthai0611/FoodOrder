package com.example.foodorder.models;

public class User {
    private int id;
    private String username;
    private String token;

    // Constructor
    public User(int id, String username, String token) {
        this.id = id;
        this.username = username;
        this.token = token;
    }

    // Getter
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }
}
