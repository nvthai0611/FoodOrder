package com.example.foodorder.network;

import com.example.foodorder.models.Food;
import com.example.foodorder.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {
    @GET("/user/{id}")
    Call<User> getUserById(@Path("id") String id);

    @PUT("/user/{id}")
    Call<User> updateUser(@Path("id") String id, @Body User user);

    @GET("/user/{id}/favorite-foods")
    Call<List<Food>> getFavoriteFoodsByUserId(@Path("id") String userId);
}
