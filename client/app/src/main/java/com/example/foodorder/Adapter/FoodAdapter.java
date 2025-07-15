package com.example.foodorder.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodorder.R;
import com.example.foodorder.activity.FoodActivity;
import com.example.foodorder.activity.HomeActivity;
import com.example.foodorder.models.Food;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder>{
    private List<Food> foodList;
    private Context context;

    public FoodAdapter(Context context, List<Food> foodList) {
        this.context = context;
        this.foodList = foodList;
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFood;
        TextView tvName, tvDesc, tvPrice;
        RatingBar stars;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFood = itemView.findViewById(R.id.ivFood);
            tvName = itemView.findViewById(R.id.tvFoodName);
            tvDesc = itemView.findViewById(R.id.tvFoodDesc);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            stars = itemView.findViewById(R.id.ratingBar);
        }
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food food = foodList.get(position);

        // Load image bằng Glide
        Glide.with(holder.itemView.getContext())
                .load(food.getImageUrl())
                .placeholder(R.drawable.sample_food)
                .error(R.drawable.sample_food)
                .into(holder.ivFood);

        holder.tvName.setText(food.getName());
        holder.tvDesc.setText(food.getDescription());
        holder.tvPrice.setText(String.format("$%.2f", food.getPrice()));

        holder.stars.setRating((float) food.getRating());

        // Gán sự kiện click để chuyển sang FoodActivity
        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, FoodActivity.class);

            // Truyền dữ liệu qua intent
            intent.putExtra("id", food.getId());
            intent.putExtra("category", food.getCategory()); // gửi cả object
            intent.putExtra("name", food.getName());
            intent.putExtra("description", food.getDescription());
            intent.putExtra("imageUrl", food.getImageUrl());
            intent.putExtra("price", (float) food.getPrice());
//            intent.putExtra("rating", food.getRating());

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }
}
