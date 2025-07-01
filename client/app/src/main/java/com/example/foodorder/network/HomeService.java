package com.example.foodorder.network;


import com.example.foodorder.models.Food;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;

public interface  HomeService {
    @GET("/food/all-food")
    Call<List<Food>> getAllFood();
}


