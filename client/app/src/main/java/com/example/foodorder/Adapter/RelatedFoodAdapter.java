package com.example.foodorder.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodorder.R;
import com.example.foodorder.activity.FoodActivity;
import com.example.foodorder.models.Food;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class RelatedFoodAdapter extends RecyclerView.Adapter<RelatedFoodAdapter.RelatedFoodViewHolder> {
    private final List<Food> items;
    private final Context context;

    public RelatedFoodAdapter(Context context, List<Food> items) {
        this.context = context;
        this.items = items;
    }

    public static class RelatedFoodViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name, price, rating;

        public RelatedFoodViewHolder(@NonNull View view) {
            super(view);
            img = view.findViewById(R.id.imgRelatedFood);
            name = view.findViewById(R.id.tvRelatedFoodName);
            price = view.findViewById(R.id.tvRelatedFoodPrice);
            rating = view.findViewById(R.id.tvRelatedFoodRating);
        }
    }

    @NonNull
    @Override
    public RelatedFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_related_food, parent, false);
        return new RelatedFoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RelatedFoodViewHolder holder, int position) {
        Food item = items.get(position);

        holder.name.setText(item.getName());
        holder.price.setText(formatCurrency(item.getPrice()));
        holder.rating.setText("⭐ " + item.getRating());

        Glide.with(context)
                .load(item.getImageUrl())
                .placeholder(R.drawable.sample_food)
                .into(holder.img);

        // 👉 Khi click vào item sẽ mở lại FoodActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, FoodActivity.class);
            intent.putExtra("name", item.getName());
            intent.putExtra("description", item.getDescription());
            intent.putExtra("imageUrl", item.getImageUrl());
            intent.putExtra("price", (float) item.getPrice());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Đảm bảo hoạt động trong mọi context
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private String formatCurrency(double price) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return formatter.format(price);
    }
}
