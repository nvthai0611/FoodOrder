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
        ImageView[] stars = new ImageView[5];

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFood = itemView.findViewById(R.id.ivFood);
            tvName = itemView.findViewById(R.id.tvFoodName);
            tvDesc = itemView.findViewById(R.id.tvFoodDesc);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            stars[0] = itemView.findViewById(R.id.star1);
            stars[1] = itemView.findViewById(R.id.star2);
            stars[2] = itemView.findViewById(R.id.star3);
            stars[3] = itemView.findViewById(R.id.star4);
            stars[4] = itemView.findViewById(R.id.star5);
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
        holder.ivFood.setImageResource(R.drawable.sample_food);
        holder.tvName.setText(food.getName());
        holder.tvDesc.setText(food.getDescription());
        holder.tvPrice.setText("$" + food.getPrice());

        for (int i = 0; i < 5; i++) {
            holder.stars[i].setVisibility(View.VISIBLE);
        }
        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, FoodActivity.class);

            // Truyền dữ liệu nếu cần
            intent.putExtra("name", food.getName());
            intent.putExtra("description", food.getDescription());
//            intent.putExtra("imageResId", food.getImageResId());
//            intent.putExtra("price", food.getPrice());
//            intent.putExtra("rating", food.getRating());

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }
}
