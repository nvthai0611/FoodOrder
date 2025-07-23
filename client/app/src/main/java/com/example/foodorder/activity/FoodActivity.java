package com.example.foodorder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodorder.R;
import com.example.foodorder.Adapter.RelatedFoodAdapter;
import com.example.foodorder.models.Cart;
import com.example.foodorder.models.CartItem;
import com.example.foodorder.models.Category;
import com.example.foodorder.models.Food;
import com.example.foodorder.models.Review;
import com.example.foodorder.network.ApiClient;
import com.example.foodorder.network.FeedBackService;
import com.example.foodorder.network.CartService;
import com.example.foodorder.network.FoodService;
import com.example.foodorder.utils.FavoriteManager;
import com.example.foodorder.utils.RoutingUtils;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodActivity extends AppCompatActivity {

    private int quantity = 1;
    private TextView tvQuantity, tvPrice, tvRating;
    private double foodPrice = 0;

    private float rating = 0;

    private RecyclerView recyclerRelated;
    private Button btnAddToCart;
    private String categoryId;
    private String foodId;

    private FavoriteManager favoriteManager;

    private Food currentFood;

    private CartService cartService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fooddetail);


        // Khởi tạo FavoriteManager
        favoriteManager = new FavoriteManager(this);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        foodId = intent.getStringExtra("id");
        Category category = (Category) getIntent().getSerializableExtra("category");
        if (category != null) {
            categoryId = category.getId();
            // dùng thêm category.getName(), ...
        }
        String name = intent.getStringExtra("name");
        String description = intent.getStringExtra("description");
        String imageUrl = intent.getStringExtra("imageUrl");
        foodPrice = intent.getDoubleExtra("price", 0);
        rating = (float) intent.getDoubleExtra("rating", 0);

        cartService = ApiClient.getClient().create(CartService.class);

        // Tạo đối tượng Food hiện tại
        currentFood = new Food();
        currentFood.setId(foodId);
        currentFood.setName(name);
        currentFood.setDescription(description);
        currentFood.setImageUrl(imageUrl);
        currentFood.setPrice(foodPrice);
        currentFood.setRating(rating);
        currentFood.setCategory(category);
        // Ánh xạ View
        TextView tvName = findViewById(R.id.tvFoodName);
        tvQuantity = findViewById(R.id.tvQuantity);
        tvPrice = findViewById(R.id.tvPrice);
        tvRating = findViewById(R.id.tvRating);
        TextView tvDesc = findViewById(R.id.tvDescription);
        ImageView ivFood = findViewById(R.id.imgFood);
        ImageButton btnBack = findViewById(R.id.btnBack);
        ImageButton btnIncrease = findViewById(R.id.btnIncrease);
        ImageButton btnDecrease = findViewById(R.id.btnDecrease);
        ImageButton btnFavorite = findViewById(R.id.btnFavorite);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        recyclerRelated = findViewById(R.id.recyclerRelatedItems);

        // Gán dữ liệu
        tvName.setText(name);
        tvDesc.setText(description);
        tvQuantity.setText(String.valueOf(quantity));
        tvPrice.setText(formatCurrency(foodPrice));
        tvRating.setText(String.format(Locale.US, "%.1f", rating)); // hiển thị đúng số
        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.sample_food)
                .into(ivFood);

        // Kiểm tra trạng thái yêu thích ban đầu
        boolean isFavorite = favoriteManager.isFavorite(foodId);
        updateFavoriteIcon(btnFavorite, isFavorite);

        // Xử lý khi nhấn nút Favorite
        btnFavorite.setOnClickListener(v -> {
            boolean currentlyFavorite = favoriteManager.isFavorite(foodId);

            if (currentlyFavorite) {
                favoriteManager.removeFavorite(foodId);
                Toast.makeText(FoodActivity.this, "Đã xóa khỏi yêu thích!", Toast.LENGTH_SHORT).show();
            } else {
                favoriteManager.addFavorite(currentFood);
                Toast.makeText(FoodActivity.this, "Đã thêm vào yêu thích!", Toast.LENGTH_SHORT).show();
            }

            updateFavoriteIcon(btnFavorite, !currentlyFavorite);
        });

        // Tăng số lượng
        btnIncrease.setOnClickListener(v -> {
            quantity++;
            tvQuantity.setText(String.valueOf(quantity));
            updateTotalPrice();
        });

        // Giảm số lượng
        btnDecrease.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                tvQuantity.setText(String.valueOf(quantity));
                updateTotalPrice();
            }
        });

        // Thêm vào giỏ hàng
        btnAddToCart.setOnClickListener(v -> {
            String userId = getSharedPreferences("MyAppPrefs", MODE_PRIVATE).getString("uId", null);

            if (userId == null) {
                RoutingUtils.redirect(this, LoginActivity.class, RoutingUtils.NO_EXTRAS, RoutingUtils.ACTIVITY_FINISH);
            } else {
                CartItem item = new CartItem(foodId, name, foodPrice, quantity, imageUrl);

                Cart cart = new Cart();
                cart.setUserId(userId);
                cart.setCartItems(item);
                Call<Cart> cartCall = cartService.createOrUpdateCart(cart);

                cartCall.enqueue(new Callback<Cart>() {
                    @Override
                    public void onResponse(@NonNull Call<Cart> call, @NonNull Response<Cart> response) {
                        if (response.isSuccessful()) {
                            String message = quantity + " x " + name + " đã được thêm vào giỏ hàng!";
                            Toast.makeText(FoodActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                String errorMsg = response.errorBody().string();
                                Log.e("FOO_ACT", errorMsg);
                                Toast.makeText(FoodActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                Toast.makeText(FoodActivity.this, "Không thể thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Cart> call, @NonNull Throwable t) {
                        Toast.makeText(FoodActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                Intent backIntent = new Intent(FoodActivity.this, HomeActivity.class);
                backIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(backIntent);
                finish();
            }
        });

        // Quay lại Home
        btnBack.setOnClickListener(v -> {
            Intent backIntent = new Intent(FoodActivity.this, HomeActivity.class);
            backIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(backIntent);
            finish();
        });

        // Setup danh sách món liên quan
        recyclerRelated.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        fetchRelatedFoods(categoryId, foodId);
        fetchReviews(foodId);
    }

    // Cập nhật tổng tiền
    private void updateTotalPrice() {
        double totalPrice = foodPrice * quantity;
        btnAddToCart.setText(getString(R.string.add_to_cart) + " - " + formatCurrency(totalPrice));
    }

    // Format tiền
    private String formatCurrency(double price) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return formatter.format(price);
    }

    // Gọi API lấy món liên quan
    private void fetchRelatedFoods(String categoryId, String foodId) {
        FoodService foodService = ApiClient.getClient().create(FoodService.class);
        Call<List<Food>> call = foodService.getRelateFoodByCategoryId(categoryId, foodId);

        call.enqueue(new Callback<List<Food>>() {
            @Override
            public void onResponse(Call<List<Food>> call, Response<List<Food>> response) {
                if (response.body() != null) {
                    List<Food> relatedFoods = response.body();
                    RelatedFoodAdapter adapter = new RelatedFoodAdapter(FoodActivity.this, relatedFoods);
                    recyclerRelated.setAdapter(adapter);
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
    private void displayReviews(List<Review> reviews) {
        LinearLayout reviewContainer = findViewById(R.id.reviewContainer);
        reviewContainer.removeAllViews(); // clear cũ nếu có

        LayoutInflater inflater = LayoutInflater.from(this);

        for (Review review : reviews) {
            View reviewView = inflater.inflate(R.layout.item_review, reviewContainer, false);

            TextView tvReviewerName = reviewView.findViewById(R.id.tvReviewerName);
            TextView tvRating = reviewView.findViewById(R.id.tvReviewRating);
            TextView tvTimeAgo = reviewView.findViewById(R.id.tvReviewTime);
            TextView tvContent = reviewView.findViewById(R.id.tvReviewDes);

            tvReviewerName.setText(review.getUserName());
            tvRating.setText(String.format(Locale.US, "%d", review.getRating()));
            tvTimeAgo.setText(" • " + review.getCreated_at()); // ví dụ: "2 days ago"
            tvContent.setText(review.getComment());

            reviewContainer.addView(reviewView);
        }
    }

    private void fetchReviews(String foodId) {
        FeedBackService reviewService = ApiClient.getClient().create(FeedBackService.class);
        Call<List<Review>> call = reviewService.getReviewsByFoodId(foodId);

        call.enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Review> reviews = response.body();
                    // Log toàn bộ danh sách
                    Log.d("API_RESPONSE", "Số review: " + reviews.size());
                    for (Review review : reviews) {
                        Log.d("API_RESPONSE", "User ID: " + review.getUser_id()
                                + ", Rating: " + review.getRating()
                                + ", Comment: " + review.getComment()
                                + ", Time: " + review.getCreated_at()
                                + ", name: " + review.getUserName()
                        )

                        ;
                    }

                    // Gọi method hiển thị ra layout
                    displayReviews(reviews);
                }
            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {
                Toast.makeText(FoodActivity.this, "Không thể tải đánh giá", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Cập nhật icon yêu thích
    private void updateFavoriteIcon(ImageButton button, boolean isFavorite) {
        if (isFavorite) {
            button.setImageResource(R.drawable.baseline_favorite_24);
        } else {
            button.setImageResource(R.drawable.baseline_favorite_border_24);
        }
    }

    // Xử lý Back vật lý
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(FoodActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
