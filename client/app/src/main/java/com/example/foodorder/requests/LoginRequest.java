package com.example.foodorder.requests;


public class LoginRequest {
    private String username;
    private String password;

    public LoginRequest(String username, String password){
        this.username = username;
        this.password = password;
    }

    // getters nếu cần
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

