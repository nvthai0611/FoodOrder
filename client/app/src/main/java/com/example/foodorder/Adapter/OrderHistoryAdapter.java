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
import com.example.foodorder.models.OrderPreview;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder> {

    private Context context;
    private List<OrderPreview> orderList;
    private List<OrderPreview> fullOrderList; // danh sách gốc không lọc

    public OrderHistoryAdapter(Context context, List<OrderPreview> orderList) {
        this.context = context;
        this.orderList = new ArrayList<>(orderList);
        this.fullOrderList = new ArrayList<>(orderList);
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_history, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderPreview order = orderList.get(position);

        holder.tvOrderCode.setText("Mã đơn: #" + order.getOrderId());
        holder.tvOrderDate.setText(order.getCreatedAt());
        holder.tvOrderStatus.setText(order.getStatus());
        holder.tvTotalPrice.setText("Tổng tiền: " + order.getTotalPrice() + "đ");

        List<String> images = order.getFoodImages();

        // Hiển thị ảnh 1
        if (images != null && images.size() > 0 && images.get(0) != null) {
            int resId1 = context.getResources().getIdentifier(images.get(0), "drawable", context.getPackageName());
            holder.imgProduct1.setImageResource(resId1 != 0 ? resId1 : R.drawable.sample_food);
        } else {
            holder.imgProduct1.setImageResource(R.drawable.sample_food);
        }

        // Hiển thị ảnh 2
        if (images != null && images.size() > 1 && images.get(1) != null) {
            int resId2 = context.getResources().getIdentifier(images.get(1), "drawable", context.getPackageName());
            holder.imgProduct2.setImageResource(resId2 != 0 ? resId2 : R.drawable.sample_food);
        } else {
            holder.imgProduct2.setImageResource(R.drawable.sample_food);
        }

        // Hiển thị "+x món"
        if (images != null && images.size() > 2) {
            holder.tvMoreProducts.setVisibility(View.VISIBLE);
            holder.tvMoreProducts.setText("+" + (images.size() - 2) + " món");
        } else {
            holder.tvMoreProducts.setVisibility(View.GONE);
        }

        holder.btnViewDetail.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrderDetailActivity.class);
            intent.putExtra("order_id", order.getOrderId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    // ✅ Hàm lọc đơn hàng theo từ khóa (order_id)
    public void filterByOrderId(String keyword) {
        orderList.clear();
        if (keyword == null || keyword.trim().isEmpty()) {
            orderList.addAll(fullOrderList);
        } else {
            for (OrderPreview order : fullOrderList) {
                if (order.getOrderId().toLowerCase().contains(keyword.toLowerCase())) {
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
