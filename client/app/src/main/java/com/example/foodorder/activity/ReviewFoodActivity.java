package com.example.foodorder.activity;

import android.os.Bundle;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
            int foodId = getIntent().getIntExtra("food_id", -1);
            foodQuantity = getIntent().getIntExtra("food_quantity", 1);
            
            // Normally you would fetch the food details from your database or API
            // For this example, let's assume we're getting the food object directly
            loadFoodDetails(foodId);
        }
        
        // Setup star rating
        setupRatingStars();
        
        // Submit button
        btnSubmit.setOnClickListener(v -> submitReview());
    }
    
    private void loadFoodDetails(int foodId) {
        // Here you would load the food details from your database or API
        // For this example, we'll simulate it
        // In a real app, you would replace this with actual data fetching
        
        // Simulated food data - in reality, you would fetch this based on foodId
//        food = new Food();
//        food.setId(foodId);
//        food.setName("Sample Food Name");
//        food.setImage("https://example.com/image.jpg"); // Example URL
//
//        // Update UI with food information
//        tvFoodName.setText(food.getName());
//        tvFoodQuantity.setText("Số lượng: " + foodQuantity);
//
//        // Load food image using Glide
//        Glide.with(this)
//                .load(food.getImage())
//                .placeholder(R.drawable.placeholder_image)
//                .error(R.drawable.error_image)
//                .into(imgFood);
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
        
        // Create a new review
        Review review = new Review();
        review.setFood_id(food.getId());
        review.setRating(currentRating);
        review.setComment(comment);
        
        // For userId, you would get this from the logged-in user
        // Assuming you have a userId from your authentication system
        String userId = "1"; // Replace with actual user ID
        review.setUser_id(userId);
        
        // Set current time for createdAt
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentDateTime = sdf.format(new Date());
        review.setCreated_at(currentDateTime);
        
        // Here you would save the review to your database or send it to your API
        saveReview(review);
    }
    
    private void saveReview(Review review) {
        // Here you would implement the logic to save the review
        // This could be a database operation or API call
        
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
