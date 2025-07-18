package com.example.foodorder.models;


public class User {
    private String id;
    private String name;
    private String token;
    private String email;
    private String password;
    private String phone;
    private String role;       // "user" hoặc "admin"
    private boolean isDeleted;
    private String createdAt;

    public User() {}

    public User(String id, String name, String token, String username, String password, String email, String phone, String role, boolean isDeleted, String createdAt) {
        this.id = id;
        this.name = name;
        this.token = token;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
    }

    public User(String id, String name, String email, String phone, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}


