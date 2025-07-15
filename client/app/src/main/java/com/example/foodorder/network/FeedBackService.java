package com.example.foodorder.network;

import com.example.foodorder.models.Food;
import com.example.foodorder.models.Review;
import com.example.foodorder.requests.ReviewRequest;
import com.example.foodorder.response.ReviewResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FeedBackService {
    @GET("/food/by-id/{foodId}")
    Call<Food> getFoodById(@Path("foodId") String foodId);

    @POST("/review/create")
    Call<ReviewResponse> createReview(@Body ReviewRequest review);

    @GET("/review/getByFoodId/{foodId}")
    Call<List<Review>> getReviewsByFoodId(@Path("foodId") String foodId);
}
