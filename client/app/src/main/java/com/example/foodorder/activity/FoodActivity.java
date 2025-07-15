package com.example.foodorder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodorder.R;
import com.example.foodorder.Adapter.RelatedFoodAdapter;
import com.example.foodorder.models.Food;
import com.example.foodorder.network.ApiClient;
import com.example.foodorder.network.FoodService;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodActivity extends AppCompatActivity {

    private int quantity = 1;
    private TextView tvQuantity, tvPrice;
    private float foodPrice = 0;
    private RecyclerView recyclerRelated;
    private Button btnAddToCart;
    private String categoryId;
    private String foodId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fooddetail);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        foodId = intent.getStringExtra("id");
        categoryId = intent.getStringExtra("categoryId"); // <-- Nhận foodId để gọi API
        String name = intent.getStringExtra("name");
        String description = intent.getStringExtra("description");
        String imageUrl = intent.getStringExtra("imageUrl");
        foodPrice = intent.getFloatExtra("price", 0);

        // Ánh xạ View
        TextView tvName = findViewById(R.id.tvFoodName);
        tvQuantity = findViewById(R.id.tvQuantity);
        tvPrice = findViewById(R.id.tvPrice);
        TextView tvDesc = findViewById(R.id.tvDescription);
        ImageView ivFood = findViewById(R.id.imgFood);
        ImageButton btnBack = findViewById(R.id.btnBack);
        ImageButton btnIncrease = findViewById(R.id.btnIncrease);
        ImageButton btnDecrease = findViewById(R.id.btnDecrease);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        recyclerRelated = findViewById(R.id.recyclerRelatedItems);

        // Gán dữ liệu
        tvName.setText(name);
        tvDesc.setText(description);
        tvQuantity.setText(String.valueOf(quantity));
        tvPrice.setText(formatCurrency(foodPrice));

        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.sample_food)
                .into(ivFood);

        // Sự kiện tăng số lượng
        btnIncrease.setOnClickListener(v -> {
            quantity++;
            tvQuantity.setText(String.valueOf(quantity));
            updateTotalPrice();
        });

        // Sự kiện giảm số lượng
        btnDecrease.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                tvQuantity.setText(String.valueOf(quantity));
                updateTotalPrice();
            }
        });

        // Sự kiện nút "Thêm vào giỏ"
        btnAddToCart.setOnClickListener(v -> {
            String message = quantity + " x " + name + " đã được thêm vào giỏ hàng!";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        });

        // Nút quay lại Home
        btnBack.setOnClickListener(v -> {
            Intent backIntent = new Intent(FoodActivity.this, HomeActivity.class);
            backIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(backIntent);
            finish();
        });

        // Setup RecyclerView
        recyclerRelated.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Gọi API lấy món liên quan
        fetchRelatedFoods(categoryId, foodId);
    }

    // Gọi API để lấy danh sách món liên quan
    private void fetchRelatedFoods(String categoryId, String foodId) {
        FoodService foodService = ApiClient.getClient().create(FoodService.class);
        Call<List<Food>> call = foodService.getRelateFoodByCategoryId(categoryId, foodId);

        call.enqueue(new Callback<List<Food>>() {
            @Override
            public void onResponse(Call<List<Food>> call, Response<List<Food>> response) {
                if (response.body() != null) {
                    List<Food> relatedFoods = response.body();

                    // Log từng món ăn trong danh sách
                    for (Food food : relatedFoods) {
                        Log.d("RelatedFood", "ID: " + food.getId()
                                + ", Name: " + food.getName()
                                + ", Price: " + food.getPrice()
                                + ", Rating: " + food.getRating());
                    }

                    // Set adapter cho RecyclerView
                    RelatedFoodAdapter relatedAdapter = new RelatedFoodAdapter(FoodActivity.this, relatedFoods);
                    recyclerRelated.setAdapter(relatedAdapter);
                } else {
                    Toast.makeText(FoodActivity.this, "Không tìm thấy món liên quan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Food>> call, Throwable t) {
                Toast.makeText(FoodActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Cập nhật tổng tiền
    private void updateTotalPrice() {
        float totalPrice = foodPrice * quantity;
        btnAddToCart.setText(getString(R.string.add_to_cart) + " - " + formatCurrency(totalPrice));
    }

    // Format tiền theo VNĐ
    private String formatCurrency(float price) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return formatter.format(price);
    }

    // Xử lý nút Back vật lý
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(FoodActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
