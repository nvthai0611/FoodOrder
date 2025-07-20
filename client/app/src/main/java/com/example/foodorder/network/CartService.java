package com.example.foodorder.network;

import com.example.foodorder.models.Cart;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CartService {
    @GET("/cart/{userId}")
    Call<Cart> getCart(@Path("userId") String userId);

    @POST("/cart")
    Call<Cart> createOrUpdateCart(@Body Cart cart);
}
