package com.example.foodorder.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.foodorder.Adapter.BannerAdapter;
import com.example.foodorder.Adapter.FoodAdapter;
import com.example.foodorder.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FoodAdapter adapter;
    List<FoodDemo> foodList;
    ViewPager2 viewPager;
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        viewPager = findViewById(R.id.bannerViewPager);
        //tabLayout = findViewById(R.id.bannerIndicator);
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

//        // Gắn TabLayout indicator
//        new TabLayoutMediator(tabLayout, viewPager,
//                (tab, position) -> {
//                    // Không cần làm gì ở đây nếu chỉ là dot indicator
//                }).attach();
    }

    private List<FoodDemo> getFakeFoodList() {
        List<FoodDemo> list = new ArrayList<>();
        list.add(new FoodDemo("Double Decker", "Beef Burger", 35.0, R.drawable.sample_food, 5));
        list.add(new FoodDemo("Smoke House", "Chicken Burger", 30.0, R.drawable.sample_food, 4));
        list.add(new FoodDemo("Vegetable Salad", "Lettuce and Tomatoes", 15.0, R.drawable.sample_food, 5));
        list.add(new FoodDemo("Chocobar", "Vanilla and Nuts", 5.0, R.drawable.sample_food, 3));
        return list;
    }

    public class FoodDemo {
        private String name;
        private String description;
        private double price;
        private int imageResId;
        private int rating;

        public FoodDemo(String name, String description, double price, int imageResId, int rating) {
            this.name = name;
            this.description = description;
            this.price = price;
            this.imageResId = imageResId;
            this.rating = rating;
        }

        // Getters
        public String getName() { return name; }
        public String getDescription() { return description; }
        public double getPrice() { return price; }
        public int getImageResId() { return imageResId; }
        public int getRating() { return rating; }
    }

}

