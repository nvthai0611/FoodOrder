package com.example.foodorder.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import java.util.Map;

public interface SendPasswordService {
    @POST("/send-password")
    Call<Void> sendPassword(@Body Map<String, String> body);
}
