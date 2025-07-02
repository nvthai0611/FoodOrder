package com.example.foodorder.network;

import com.example.foodorder.models.OrderPreview;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface OrderService {

    @GET("orders/{userId}")
    Call<List<OrderPreview>> getOrdersByUser(@Path("userId") String userId);
}
