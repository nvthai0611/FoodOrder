package com.example.foodorder.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodorder.R;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "FoodAPI";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        // Kiểm tra SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String uId = sharedPreferences.getString("uId", null);
        String uName = sharedPreferences.getString("uName", null);
        String uEmail = sharedPreferences.getString("uEmail", null);
        if (uId != null && uName != null && uEmail != null) {
            // Nếu đã đăng nhập trước đó thì vào Home luôn
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish(); // đóng MainActivity
            return;
        }

        // Xử lý nút chuyển sang màn Login
        Button btnGoToLogin = findViewById(R.id.btnGoToLogin);
        btnGoToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // Xử lý nút chuyển sang màn Home
        Button btnGoToHome = findViewById(R.id.btnGoToHome);
        btnGoToHome.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
        });
    }
}
