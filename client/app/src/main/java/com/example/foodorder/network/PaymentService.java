package com.example.foodorder.network;

import com.example.foodorder.models.Food;
import com.example.foodorder.models.PaymentInfo;
import com.example.foodorder.response.PaymentCheckResponse;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PaymentService {
    @POST("/create-payment-info")
    Call<PaymentInfo> createPaymentInfo(@Body Map<String, String> body);

    @POST("/check-payment-status")
    Call<PaymentCheckResponse> checkPaymentStatus(@Body Map<String, String> body);
}
