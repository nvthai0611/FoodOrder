package com.example.foodorder.response;

import com.example.foodorder.models.User;

public class LoginResponse {
    private boolean success;
    private String message;
    private User user;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public User getUser() { return user; }
}