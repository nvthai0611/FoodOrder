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

import com.bumptech.glide.Glide;
import com.example.foodorder.R;
import com.example.foodorder.activity.OrderDetailActivity;
import com.example.foodorder.models.Food;
import com.example.foodorder.models.Order;
import com.example.foodorder.models.OrderItem;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder> {

    private final Context context;
    private final List<Order> fullOrderList;
    private final OnOrderClickListener onOrderClickListener;
    private List<Order> orderList;

    // Nếu ảnh là tên file, bạn có thể đặt base URL tại đây
    private static final String BASE_IMAGE_URL = "https://yourdomain.com/images/"; // Cập nhật đúng domain của bạn

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
        holder.tvOrderStatus.setText(convertStatus(order.getStatus()));
        holder.tvTotalPrice.setText("Tổng tiền: " + formatMoney(order.getTotal_price()));

        List<OrderItem> items = order.getItems();
        List<String> imageUrls = new ArrayList<>();
        if (items != null) {
            for (OrderItem item : items) {
                Food food = item.getFood_id();
                if (food != null && food.getImageUrl() != null) {
                    String imageUrl = food.getImageUrl();

                    // Nếu image chỉ là tên file, thêm BASE_IMAGE_URL
                    if (!imageUrl.startsWith("http")) {
                        imageUrl = BASE_IMAGE_URL + imageUrl;
                    }

                    imageUrls.add(imageUrl);
                }
            }
        }

        // Load ảnh 1 bằng Glide
        if (imageUrls.size() > 0) {
            Glide.with(context)
                    .load(imageUrls.get(0))
                    .placeholder(R.drawable.sample_food)
                    .error(R.drawable.sample_food)
                    .into(holder.imgProduct1);
        } else {
            holder.imgProduct1.setImageResource(R.drawable.sample_food);
        }

        // Load ảnh 2 bằng Glide
        if (imageUrls.size() > 1) {
            Glide.with(context)
                    .load(imageUrls.get(1))
                    .placeholder(R.drawable.sample_food)
                    .error(R.drawable.sample_food)
                    .into(holder.imgProduct2);
        } else {
            holder.imgProduct2.setImageResource(R.drawable.sample_food);
        }

        // Hiển thị "+x món" nếu > 2
        if (imageUrls.size() > 2) {
            holder.tvMoreProducts.setVisibility(View.VISIBLE);
            holder.tvMoreProducts.setText("+" + (imageUrls.size() - 2) + " món");
        } else {
            holder.tvMoreProducts.setVisibility(View.GONE);
        }

        // Mở chi tiết đơn
        holder.btnViewDetail.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrderDetailActivity.class);
            intent.putExtra("order_id", order.get_id());
            context.startActivity(intent);
        });

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

    private String convertStatus(String status) {
        switch (status) {
            case "pending": return "Chờ xác nhận";
            case "preparing": return "Đang chuẩn bị";
            case "done": return "Đã giao";
            case "canceled": return "Đã huỷ";
            default: return status;
        }
    }

    private String formatMoney(int money) {
        return NumberFormat.getNumberInstance(Locale.getDefault()).format(money) + "đ";
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
