package com.example.foodorder.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodorder.R;
import com.example.foodorder.models.Food;
import com.example.foodorder.models.OrderItem;

import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ViewHolder> {

    private final Context context;
    private final List<OrderItem> itemList;

    public OrderItemAdapter(Context context, List<OrderItem> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFoodItem;
        TextView tvFoodName, tvQuantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFoodItem = itemView.findViewById(R.id.imgFoodItem);
            tvFoodName = itemView.findViewById(R.id.tvFoodName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
        }
    }

    @NonNull
    @Override
    public OrderItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_food, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemAdapter.ViewHolder holder, int position) {
        OrderItem item = itemList.get(position);
        Food food = item.getFood_id();

        if (food != null) {
            holder.tvFoodName.setText(food.getName());

            // Dùng Glide với tên ảnh local drawable nếu chỉ là tên file ảnh
            int resId = context.getResources().getIdentifier(
                    food.getImageUrl(), "drawable", context.getPackageName()
            );
            if (resId != 0) {
                Glide.with(context)
                        .load(resId)
                        .placeholder(R.drawable.sample_food)
                        .into(holder.imgFoodItem);
            } else {
                // fallback nếu không tìm được ảnh resource
                holder.imgFoodItem.setImageResource(R.drawable.sample_food);
            }
        } else {
            holder.tvFoodName.setText("Món ăn không xác định");
            holder.imgFoodItem.setImageResource(R.drawable.sample_food);
        }

        holder.tvQuantity.setText("Số lượng: " + item.getQuantity());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
