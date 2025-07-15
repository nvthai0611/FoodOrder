package com.example.foodorder.activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.foodorder.R;
import com.example.foodorder.models.Food;
import com.example.foodorder.models.Review;
import com.example.foodorder.network.ApiClient;
import com.example.foodorder.network.FeedBackService;
import com.example.foodorder.network.HomeService;
import com.example.foodorder.requests.ReviewRequest;
import com.example.foodorder.response.ReviewResponse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewFoodActivity extends AppCompatActivity {

    private ImageView imgFood;
    private TextView tvFoodName, tvFoodQuantity;
    private ImageView star1, star2, star3, star4, star5;
    private EditText etComment;
    private Button btnSubmit;
    
    private Food food;
    private int foodQuantity;
    private int currentRating = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_food);
        
        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        
        // Initialize views
        imgFood = findViewById(R.id.imgFood);
        tvFoodName = findViewById(R.id.tvFoodName);
        tvFoodQuantity = findViewById(R.id.tvFoodQuantity);
        star1 = findViewById(R.id.star1);
        star2 = findViewById(R.id.star2);
        star3 = findViewById(R.id.star3);
        star4 = findViewById(R.id.star4);
        star5 = findViewById(R.id.star5);
        etComment = findViewById(R.id.etComment);
        btnSubmit = findViewById(R.id.btnSubmit);
        
        // Get data from intent
        if (getIntent().hasExtra("food_id")) {
            String foodId = getIntent().getStringExtra("food_id");
            foodQuantity = getIntent().getIntExtra("food_quantity", 1);

            fetchFoodById(foodId);
        }else{
            String foodId = "665f7a1a9fc4a6b7b78b1e01";
            foodQuantity = 1;
            fetchFoodById(foodId);
        }

        // Setup star rating
        setupRatingStars();
        
        // Submit button
        btnSubmit.setOnClickListener(v -> submitReview());
    }

    private void fetchFoodById(String id) {
        FeedBackService feedBackService = ApiClient.getClient().create(FeedBackService.class);
        Call<Food> call = feedBackService.getFoodById(id);

        call.enqueue(new Callback<Food>() {
            @Override
            public void onResponse(Call<Food> call, Response<Food> response) {
                if (response.isSuccessful() && response.body() != null) {
                    food = response.body();
                    tvFoodName.setText(food.getName());
                    tvFoodQuantity.setText("Số lượng: " + foodQuantity);

                    // Load food image using Glide
                    Glide.with(ReviewFoodActivity.this)
                            .load(food.getImageUrl())
                            .placeholder(R.drawable.placeholder_image)
                            .error(R.drawable.error_image)
                            .into(imgFood);
                } else {
                    Toast.makeText(ReviewFoodActivity.this, "Failed to get data", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Response error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Food> call, Throwable t) {
                Toast.makeText(ReviewFoodActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "API call failed", t);
            }
        });
    }

    private void setupRatingStars() {
        // Set click listeners for stars
        star1.setOnClickListener(v -> updateRating(1));
        star2.setOnClickListener(v -> updateRating(2));
        star3.setOnClickListener(v -> updateRating(3));
        star4.setOnClickListener(v -> updateRating(4));
        star5.setOnClickListener(v -> updateRating(5));
    }
    
    private void updateRating(int rating) {
        currentRating = rating;
        
        // Update star images based on rating
        star1.setImageResource(rating >= 1 ? R.drawable.ic_star_filled : R.drawable.ic_star_border);
        star2.setImageResource(rating >= 2 ? R.drawable.ic_star_filled : R.drawable.ic_star_border);
        star3.setImageResource(rating >= 3 ? R.drawable.ic_star_filled : R.drawable.ic_star_border);
        star4.setImageResource(rating >= 4 ? R.drawable.ic_star_filled : R.drawable.ic_star_border);
        star5.setImageResource(rating >= 5 ? R.drawable.ic_star_filled : R.drawable.ic_star_border);
    }
    
    private void submitReview() {
        if (currentRating == 0) {
            Toast.makeText(this, "Vui lòng chọn đánh giá sao", Toast.LENGTH_SHORT).show();
            return;
        }
        
        String comment = etComment.getText().toString().trim();
        if(comment.isEmpty()){
            Toast.makeText(this, "Vui lòng nhập nội dung feedback", Toast.LENGTH_SHORT).show();
            return;
        }
        // Create a new review
        ReviewRequest review = new ReviewRequest();
        review.setFoodId(food.getId());
        review.setRating(currentRating);
        review.setComment(comment);

        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("uId", "666abc000001000000000002");
        review.setUserId(userId);

        saveReview(review);
    }
    
    private void saveReview(ReviewRequest review) {
        FeedBackService feedBackService = ApiClient.getClient().create(FeedBackService.class);
        Call<ReviewResponse> call = feedBackService.createReview(review);
        call.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String message = response.body().getMessage();
                    ReviewRequest createdReview = response.body().getReview();
                    Log.d("Review", "Message: " + message + " | Review: " + createdReview.getComment());
                } else {
                    Log.e("Review", "Failed with code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                Log.e("Review", "Error: " + t.getMessage());
            }
        });
        
        // For this example, we'll just show a success message and finish the activity
        Toast.makeText(this, "Cảm ơn bạn đã đánh giá sản phẩm!", Toast.LENGTH_SHORT).show();
        finish();
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
