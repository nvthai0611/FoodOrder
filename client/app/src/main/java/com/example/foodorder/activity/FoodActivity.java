package com.example.foodorder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodorder.R;
import com.example.foodorder.adapter.RelatedFoodAdapter;
import com.example.foodorder.models.Food;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FoodActivity extends AppCompatActivity {

    private int quantity = 1; // Mặc định số lượng là 1
    private TextView tvQuantity, tvPrice;
    private float foodPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fooddetail);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String description = intent.getStringExtra("description");
        String imageUrl = intent.getStringExtra("imageUrl");
        foodPrice = intent.getFloatExtra("price", 0);

        // Ánh xạ view
        TextView tvName = findViewById(R.id.tvFoodName);
        tvQuantity = findViewById(R.id.tvQuantity);
        tvPrice = findViewById(R.id.tvPrice);
        TextView tvDesc = findViewById(R.id.tvDescription);
        ImageView ivFood = findViewById(R.id.imgFood);
        ImageButton btnBack = findViewById(R.id.btnBack);
        ImageButton btnIncrease = findViewById(R.id.btnIncrease);
        ImageButton btnDecrease = findViewById(R.id.btnDecrease);
        Button btnAddToCart = findViewById(R.id.btnAddToCart);

        // Gán dữ liệu
        tvName.setText(name);
        tvDesc.setText(description);
        tvQuantity.setText(String.valueOf(quantity));
        tvPrice.setText(formatCurrency(foodPrice)); // Luôn hiển thị đơn giá ban đầu

        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.sample_food)
                .into(ivFood);

        // Tăng số lượng
        btnIncrease.setOnClickListener(v -> {
            quantity++;
            tvQuantity.setText(String.valueOf(quantity));
            updateTotalPrice(); // Cập nhật text nút
        });

        // Giảm số lượng
        btnDecrease.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                tvQuantity.setText(String.valueOf(quantity));
                updateTotalPrice();
            }
        });

        // Nút back → quay về Home
        btnBack.setOnClickListener(v -> {
            Intent backIntent = new Intent(FoodActivity.this, HomeActivity.class);
            backIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(backIntent);
            finish();
        });

        // Nút "Thêm vào giỏ"
        btnAddToCart.setOnClickListener(v -> {
            String message = quantity + " x " + name + " đã được thêm vào giỏ hàng!";
            android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show();
        });

        // Danh sách món liên quan
        List<Food> relatedFoods = new ArrayList<>();
        relatedFoods.add(new Food("1", "Bánh cuốn", "Mềm mịn, nóng hổi", "https://i.imgur.com/tYHQVmD.jpg", 30000, 4.5));
        relatedFoods.add(new Food("2", "Bún bò", "Nước dùng đậm đà", "https://i.imgur.com/ncX4lcy.jpg", 45000, 4.6));
        relatedFoods.add(new Food("3", "Phở", "Truyền thống Việt Nam", "https://i.imgur.com/jKbN4kz.jpg", 40000, 4.8));

        RecyclerView recyclerRelated = findViewById(R.id.recyclerRelatedItems);
        RelatedFoodAdapter relatedAdapter = new RelatedFoodAdapter(this, relatedFoods);
        recyclerRelated.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerRelated.setAdapter(relatedAdapter);
    }

    // Cập nhật tổng tiền trên nút "Thêm vào giỏ"
    private void updateTotalPrice() {
        float totalPrice = foodPrice * quantity;
        Button btnAddToCart = findViewById(R.id.btnAddToCart);
        btnAddToCart.setText(getString(R.string.add_to_cart) + " - " + formatCurrency(totalPrice));
    }

    // Format tiền theo VNĐ
    private String formatCurrency(float price) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return formatter.format(price);
    }

    // Nếu ấn nút Back vật lý → cũng về Home
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(FoodActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
