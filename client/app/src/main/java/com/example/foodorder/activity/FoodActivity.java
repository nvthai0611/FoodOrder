package com.example.foodorder.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodorder.R;

public class FoodActivity extends AppCompatActivity {

    private static final String TAG = "FoodAPI";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fooddetail);
        // Lấy token từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String token = prefs.getString("token", "Không có token");
        TextView tvToken = findViewById(R.id.tvToken);
        tvToken.setText(token);
        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String description = intent.getStringExtra("description");
        int imageResId = intent.getIntExtra("imageResId", 0);
        float price = intent.getFloatExtra("price", 0);
        int rating = intent.getIntExtra("rating", 0);
        // Gán dữ liệu lên UI (ví dụ)
        TextView tvName = findViewById(R.id.tvFoodName);
//        TextView tvDesc = findViewById(R.id.tvFoodDescription);
//        TextView tvPrice = findViewById(R.id.tvFoodPrice);
//        ImageView ivFood = findViewById(R.id.ivFoodImage);

        tvName.setText(name);
//        tvDesc.setText(description);
//        tvPrice.setText("$" + price);
//        ivFood.setImageResource(imageResId);

        // Hiển thị số sao nếu cần (rating: int từ 0 đến 5)
//        ImageView[] stars = new ImageView[]{
//                findViewById(R.id.star1),
//                findViewById(R.id.star2),
//                findViewById(R.id.star3),
//                findViewById(R.id.star4),
//                findViewById(R.id.star5),
//        };
//        for (int i = 0; i < 5; i++) {
//            stars[i].setVisibility(i < rating ? View.VISIBLE : View.INVISIBLE);
//        }
    }
}
