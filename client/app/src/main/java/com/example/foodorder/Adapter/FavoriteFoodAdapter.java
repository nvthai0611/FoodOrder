package com.example.foodorder.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodorder.R;
import com.example.foodorder.activity.FoodActivity;
import com.example.foodorder.models.Food;
import com.example.foodorder.utils.FavoriteManager;
import com.google.android.material.button.MaterialButton;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class FavoriteFoodAdapter extends RecyclerView.Adapter<FavoriteFoodAdapter.FoodViewHolder> {

    private List<Food> favoriteFoods;
    private Context context;
    private FavoriteManager favoriteManager;

    public FavoriteFoodAdapter(List<Food> favoriteFoods, Context context) {
        this.favoriteFoods = favoriteFoods;
        this.context = context;
        this.favoriteManager = new FavoriteManager(context);
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food food = favoriteFoods.get(position);
        holder.tvName.setText(food.getName());
        holder.tvPrice.setText(formatCurrency(food.getPrice()));

        Glide.with(context)
                .load(food.getImageUrl())
                .placeholder(R.drawable.sample_food)
                .into(holder.ivFood);

        // Xem chi tiết món ăn
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, FoodActivity.class);
            intent.putExtra("id", food.getId());
            intent.putExtra("name", food.getName());
            intent.putExtra("description", food.getDescription());
            intent.putExtra("imageUrl", food.getImageUrl());
            intent.putExtra("price", food.getPrice());
            intent.putExtra("category", food.getCategory()); // nếu có
            context.startActivity(intent);
        });

        // Xoá khỏi yêu thích
        holder.btnRemoveFavorite.setOnClickListener(v -> {
            favoriteManager.removeFavorite(food.getId());
            favoriteFoods.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, favoriteFoods.size());
        });
    }

    @Override
    public int getItemCount() {
        return favoriteFoods.size();
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFood;
        TextView tvName, tvPrice;
        MaterialButton btnRemoveFavorite;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFood = itemView.findViewById(R.id.imgFavoriteFood);
            tvName = itemView.findViewById(R.id.tvFavoriteFoodName);
            tvPrice = itemView.findViewById(R.id.tvFavoriteFoodPrice);
            btnRemoveFavorite = itemView.findViewById(R.id.btnRemoveFavorite);
        }
    }

    // Đổi hàm formatCurrency thành:
    private String formatCurrency(double price) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return formatter.format(price);
    }
}
