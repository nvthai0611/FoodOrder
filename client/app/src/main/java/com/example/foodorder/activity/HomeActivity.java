package com.example.foodorder.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.foodorder.Adapter.BannerAdapter;
import com.example.foodorder.Adapter.FoodAdapter;
import com.example.foodorder.R;
import com.example.foodorder.base.BaseActivity;
import com.example.foodorder.models.Food;
import com.example.foodorder.network.ApiClient;
import com.example.foodorder.network.HomeService;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends BaseActivity {

    RecyclerView recyclerView;
    FoodAdapter adapter;
    List<Food> foodList = new ArrayList<>();
    ViewPager2 viewPager;
    TabLayout tabLayout;

    private static final String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        viewPager = findViewById(R.id.bannerViewPager);
        recyclerView = findViewById(R.id.rvPopularFood);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new FoodAdapter(this, foodList);
        recyclerView.setAdapter(adapter);

        fetchFoodList(); // 👈 Gọi API thật

        List<Integer> images = Arrays.asList(R.drawable.banner_home);
        BannerAdapter bannerAdapter = new BannerAdapter(images);
        viewPager.setAdapter(bannerAdapter);

        setupBottomNavigation();
    }

    private void fetchFoodList() {
        HomeService homeService = ApiClient.getClient().create(HomeService.class);
        Call<List<Food>> call = homeService.getAllFood();

        call.enqueue(new Callback<List<Food>>() {
            @Override
            public void onResponse(Call<List<Food>> call, Response<List<Food>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    foodList.clear();
                    foodList.addAll(response.body());
                    adapter.notifyDataSetChanged();

                    Log.e(TAG, "Response success:");
                    for (Food food : foodList) {
                        Log.e(TAG, "Food item: " +
                                "\n - Name: " + food.getName() +
                                "\n - Description: " + food.getDescription() +
                                "\n - Price: " + food.getPrice() +
                                "\n - Image URL: " + food.getImageUrl() +
                                "\n - Category: " + (food.getCategory() != null ? food.getCategory().getName() : "null"));
                    }
                } else {
                    Toast.makeText(HomeActivity.this, "Failed to get data", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Response error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Food>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "API call failed", t);
            }
        });
    }
}
