package com.example.foodorder.network;

import com.example.foodorder.models.User;
import com.example.foodorder.requests.LoginRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginService {
    // API login POST, gửi username/password dạng JSON
    @POST("login")
    Call<User> loginUser(@Body LoginRequest loginRequest);
}
