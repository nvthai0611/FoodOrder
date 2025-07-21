package com.example.foodorder.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorder.R;
import com.example.foodorder.activity.OrderDetailActivity;
import com.example.foodorder.models.Food;
import com.example.foodorder.models.Order;
import com.example.foodorder.models.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> orderList;
    private List<Order> fullOrderList;
    private OnOrderClickListener onOrderClickListener;

    public interface OnOrderClickListener {
        void onOrderClick(Order order);
    }

    public OrderHistoryAdapter(Context context, List<Order> orderList, OnOrderClickListener listener) {
        this.context = context;
        this.orderList = new ArrayList<>(orderList);
        this.fullOrderList = new ArrayList<>(orderList);
        this.onOrderClickListener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_history, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        holder.tvOrderCode.setText("Mã đơn: #" + order.get_id());
        holder.tvOrderDate.setText(order.getCreated_at());
        holder.tvOrderStatus.setText(order.getStatus());
        holder.tvTotalPrice.setText("Tổng tiền: " + order.getTotal_price() + "đ");

        // Lấy ảnh từ danh sách món
        List<OrderItem> items = order.getItems();
        List<String> imageNames = new ArrayList<>();
        if (items != null) {
            for (OrderItem item : items) {
                Food food = item.getFood_id();
                if (food != null && food.getImageUrl() != null) {
                    imageNames.add(food.getImageUrl());
                }
            }
        }

        // Ảnh 1
        if (imageNames.size() > 0) {
            int resId1 = context.getResources().getIdentifier(imageNames.get(0), "drawable", context.getPackageName());
            holder.imgProduct1.setImageResource(resId1 != 0 ? resId1 : R.drawable.sample_food);
        } else {
            holder.imgProduct1.setImageResource(R.drawable.sample_food);
        }

        // Ảnh 2
        if (imageNames.size() > 1) {
            int resId2 = context.getResources().getIdentifier(imageNames.get(1), "drawable", context.getPackageName());
            holder.imgProduct2.setImageResource(resId2 != 0 ? resId2 : R.drawable.sample_food);
        } else {
            holder.imgProduct2.setImageResource(R.drawable.sample_food);
        }

        // Hiển thị "+x món"
        if (imageNames.size() > 2) {
            holder.tvMoreProducts.setVisibility(View.VISIBLE);
            holder.tvMoreProducts.setText("+" + (imageNames.size() - 2) + " món");
        } else {
            holder.tvMoreProducts.setVisibility(View.GONE);
        }

        // Bấm "Xem chi tiết"
        holder.btnViewDetail.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrderDetailActivity.class);
            intent.putExtra("order_id", order.get_id());
            context.startActivity(intent);
        });

        // Bấm cả item để callback
        holder.itemView.setOnClickListener(v -> {
            if (onOrderClickListener != null) {
                onOrderClickListener.onOrderClick(order);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public void filterByOrderId(String keyword) {
        orderList.clear();
        if (keyword == null || keyword.trim().isEmpty()) {
            orderList.addAll(fullOrderList);
        } else {
            for (Order order : fullOrderList) {
                if (order.get_id().toLowerCase().contains(keyword.toLowerCase())) {
                    orderList.add(order);
                }
            }
        }
        notifyDataSetChanged();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderCode, tvOrderDate, tvOrderStatus, tvTotalPrice, tvMoreProducts;
        ImageView imgProduct1, imgProduct2;
        Button btnViewDetail;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderCode = itemView.findViewById(R.id.tvOrderCode);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvMoreProducts = itemView.findViewById(R.id.tvMoreProducts);
            imgProduct1 = itemView.findViewById(R.id.imgProduct1);
            imgProduct2 = itemView.findViewById(R.id.imgProduct2);
            btnViewDetail = itemView.findViewById(R.id.btnViewDetail);
        }
    }
}
