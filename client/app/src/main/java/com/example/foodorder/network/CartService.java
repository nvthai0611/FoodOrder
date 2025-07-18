package com.example.foodorder.network;

import com.example.foodorder.models.Cart;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CartService {
    @GET("/cart")
    Call<Cart> getCart(@Query("userId") String userId);

    @POST("/cart/create")
    Call<Cart> createCart(@Body Cart cart);

    @POST("/cart/update")
    Call<Cart> updateCart(@Body Cart cart);
}
