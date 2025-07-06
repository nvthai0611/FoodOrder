package com.example.foodorder.network;


import com.example.foodorder.models.Category;
import com.example.foodorder.models.Food;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;

public interface  HomeService {
    @GET("/food/all-foods-best-sellers")
    Call<List<Food>> getAllFoodsBestSeller();

    @GET("/food/all-foods")
    Call<List<Food>> getAllFoods();

    @GET("/category/all-categories")
    Call<List<Category>> getAllCategories();
}


