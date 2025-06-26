package com.example.foodorder.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.foodorder.Adapter.BannerAdapter;
import com.example.foodorder.Adapter.FoodAdapter;
import com.example.foodorder.R;
import com.example.foodorder.base.BaseActivity;
import com.example.foodorder.models.Food;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeActivity extends BaseActivity {

    RecyclerView recyclerView;
    FoodAdapter adapter;
    List<Food> foodList;
    ViewPager2 viewPager;
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        viewPager = findViewById(R.id.bannerViewPager);
        recyclerView = findViewById(R.id.rvPopularFood);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        foodList = getFakeFoodList();
        adapter = new FoodAdapter(this, foodList);
        recyclerView.setAdapter(adapter);

        // banner
        List<Integer> images = Arrays.asList(
                R.drawable.banner_home
        );
        BannerAdapter adapter = new BannerAdapter(images);
        viewPager.setAdapter(adapter);

        setupBottomNavigation();
    }

    private List<Food> getFakeFoodList() {
        List<Food> list = new ArrayList<>();
        list.add(new Food(1, "Double Decker", "Beef Burger","https://res.cloudinary.com/dickb1q09/image/upload/v1750522475/HoaLacRent/aezn55ktqcj3qa3zebjl.jpg","Demo", 35.0, true, ""));
        list.add(new Food(2, "Smoke House", "Chicken Burger","https://res.cloudinary.com/dickb1q09/image/upload/v1750522475/HoaLacRent/aezn55ktqcj3qa3zebjl.jpg", "DEMO", 30.0, true, ""));
        list.add(new Food(3, "Vegetable Salad", "Lettuce and Tomatoes","https://res.cloudinary.com/dickb1q09/image/upload/v1750522475/HoaLacRent/aezn55ktqcj3qa3zebjl.jpg", "Category", 15.0, true, ""));
        list.add(new Food(4, "Chocobar", "Vanilla and Nuts","https://res.cloudinary.com/dickb1q09/image/upload/v1750522475/HoaLacRent/aezn55ktqcj3qa3zebjl.jpg", "Category", 5.0, true, ""));
        list.add(new Food(4, "Chocobar", "Vanilla and Nuts","https://res.cloudinary.com/dickb1q09/image/upload/v1750522475/HoaLacRent/aezn55ktqcj3qa3zebjl.jpg", "Category", 5.0, true, ""));
        list.add(new Food(4, "Chocobar", "Vanilla and Nuts","https://res.cloudinary.com/dickb1q09/image/upload/v1750522475/HoaLacRent/aezn55ktqcj3qa3zebjl.jpg", "Category", 5.0, true, ""));
        list.add(new Food(4, "Chocobar", "Vanilla and Nuts","https://res.cloudinary.com/dickb1q09/image/upload/v1750522475/HoaLacRent/aezn55ktqcj3qa3zebjl.jpg", "Category", 5.0, true, ""));
        list.add(new Food(4, "Chocobar", "Vanilla and Nuts","https://res.cloudinary.com/dickb1q09/image/upload/v1750522475/HoaLacRent/aezn55ktqcj3qa3zebjl.jpg", "Category", 5.0, true, ""));
        return list;
    }

}

