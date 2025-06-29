package com.example.foodorder.network;

import com.example.foodorder.requests.LoginRequest;
import com.example.foodorder.response.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginService {
    // API login POST, gửi username/password dạng JSON
    @POST("/auth/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);
}
