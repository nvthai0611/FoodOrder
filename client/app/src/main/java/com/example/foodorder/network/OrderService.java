package com.example.foodorder.network;


import com.example.foodorder.models.Order;
import com.example.foodorder.models.OrderPreview;
import com.example.foodorder.models.OrderDetail;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface OrderService {

    @GET("orders/{userId}")
    Call<List<Order>> getOrdersByUser(@Path("userId") String userId);

    @GET("orders/detail/{orderId}")
    Call<OrderDetail> getOrderDetail(@Path("orderId") String orderId);

    @PUT("orders/status/{orderId}")
    Call<OrderDetail> updateOrderStatus(
            @Path("orderId") String orderId,
            @Body Map<String, String> body
    );

}
