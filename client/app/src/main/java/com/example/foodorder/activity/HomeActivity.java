package com.example.foodorder.activity;

import android.os.Bundle;
import android.util.Log;

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
import com.example.foodorder.base.BaseActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class HomeActivity extends BaseActivity {
    private Socket mSocket;

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
