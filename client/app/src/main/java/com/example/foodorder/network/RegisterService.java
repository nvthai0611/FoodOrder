package com.example.foodorder.network;

import com.example.foodorder.models.User;
import com.example.foodorder.requests.RegisterRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RegisterService {
    @POST("/user/register")
    Call<User> registerUser(@Body RegisterRequest request);
}
