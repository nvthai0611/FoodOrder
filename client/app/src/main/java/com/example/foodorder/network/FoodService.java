package com.example.foodorder.network;

import com.example.foodorder.models.Food;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FoodService {
    @GET("/food/relatefood-by-categoryId/{categoryId}/{foodId}")
    Call<List<Food>> getRelateFoodByCategoryId(
            @Path("categoryId") String categoryId,
            @Path("foodId") String foodId
    );
}
