package com.example.foodorder.base;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodorder.R;
import com.example.foodorder.activity.HomeActivity;
import com.example.foodorder.activity.LoginActivity;
import com.example.foodorder.activity.user.UserProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public abstract class BaseActivity extends AppCompatActivity {

    protected void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        if (bottomNavigationView == null) return;

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home && !(this instanceof HomeActivity)) {
                startActivity(new Intent(this, HomeActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }else if (itemId == R.id.nav_orders ) {
//                startActivity(new Intent(this, OrdersActivity.class));
//                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(this, UserProfileActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });
    }
}
