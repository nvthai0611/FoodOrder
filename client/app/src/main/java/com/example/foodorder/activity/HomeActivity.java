package com.example.foodorder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.viewpager2.widget.ViewPager2;
//
//import com.example.foodorder.Adapter.BannerAdapter;
//import com.example.foodorder.Adapter.FoodAdapter;
//import com.example.foodorder.Fragment.DemoFragment;
import com.example.foodorder.Fragment.FavoriteFoodsFragment;
import com.example.foodorder.Fragment.HomeFragment;
import com.example.foodorder.Fragment.OrderHistoryFragment;
import com.example.foodorder.Fragment.UserProfileFragment;
import com.example.foodorder.R;
import com.example.foodorder.activity.user.UserProfileActivity;
import com.example.foodorder.base.BaseActivity;
import com.example.foodorder.models.Food;
import com.example.foodorder.network.ApiClient;
import com.example.foodorder.network.HomeService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);



        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            }else if(itemId == R.id.nav_favorite){
                selectedFragment = new FavoriteFoodsFragment();
            } else if (itemId == R.id.nav_orders) {
//                Intent intent = new Intent(HomeActivity.this, OrderHistoryActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
                selectedFragment = new OrderHistoryFragment();
            } else if (itemId == R.id.nav_profile) {
//                selectedFragment = new HomeFragment();
//                Intent intent = new Intent(HomeActivity.this, UserProfileActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
                selectedFragment = new UserProfileFragment();
            }


            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }

            return true;
        });
        // Set mặc định là Home
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment())
                .commit();
    }


}
