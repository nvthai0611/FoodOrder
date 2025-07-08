package com.example.foodorder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.foodorder.R;

import java.text.NumberFormat;
import java.util.Locale;

public class FoodActivity extends AppCompatActivity {

    private int quantity = 1; // Số lượng mặc định

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fooddetail);

        // 1. Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String description = intent.getStringExtra("description");
        String imageUrl = intent.getStringExtra("imageUrl");
        float price = intent.getFloatExtra("price", 0);
        int rating = intent.getIntExtra("rating", 0);

        // 2. Ánh xạ UI
        TextView tvName = findViewById(R.id.tvFoodName);
        TextView tvDesc = findViewById(R.id.tvDescription);
        TextView tvPrice = findViewById(R.id.tvPrice);
        ImageView ivFood = findViewById(R.id.imgFood);
        TextView tvQuantity = findViewById(R.id.tvQuantity);
        Button btnIncrease = findViewById(R.id.btnIncrease);
        Button btnDecrease = findViewById(R.id.btnDecrease);
        ImageButton btnBack = findViewById(R.id.btnBack);

        // 3. Gán dữ liệu
        tvName.setText(name);
        tvDesc.setText(description);
        tvPrice.setText(formatCurrency(price));

        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.sample_food)
                .into(ivFood);

        // 4. Hiển thị sao
        ImageView[] stars = {
                findViewById(R.id.star1),
                findViewById(R.id.star2),
                findViewById(R.id.star3),
                findViewById(R.id.star4),
                findViewById(R.id.star5),
        };

        for (int i = 0; i < stars.length; i++) {
            stars[i].setVisibility(i < rating ? View.VISIBLE : View.INVISIBLE);
        }

        // 5. Tăng/giảm số lượng
        tvQuantity.setText(String.valueOf(quantity));

        btnIncrease.setOnClickListener(v -> {
            quantity++;
            tvQuantity.setText(String.valueOf(quantity));
        });

        btnDecrease.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                tvQuantity.setText(String.valueOf(quantity));
            }
        });

        // 6. Back
        btnBack.setOnClickListener(v -> finish());
    }

    // Format giá tiền (VNĐ)
    private String formatCurrency(float price) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return formatter.format(price);
    }
}
