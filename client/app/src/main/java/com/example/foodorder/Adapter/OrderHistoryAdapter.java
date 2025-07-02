package com.example.foodorder.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorder.R;
import com.example.foodorder.models.OrderPreview;

import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder> {

    private Context context;
    private List<OrderPreview> orderList;

    public OrderHistoryAdapter(Context context, List<OrderPreview> orderList) {
        this.context = context;
        this.orderList = orderList;
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
            if (resId1 != 0) {
                holder.imgProduct1.setImageResource(resId1);
            } else {
                holder.imgProduct1.setImageResource(R.drawable.sample_food);
            }
        } else {
            holder.imgProduct1.setImageResource(R.drawable.sample_food);
        }

        // Hiển thị ảnh 2
        if (images != null && images.size() > 1 && images.get(1) != null) {
            int resId2 = context.getResources().getIdentifier(images.get(1), "drawable", context.getPackageName());
            if (resId2 != 0) {
                holder.imgProduct2.setImageResource(resId2);
            } else {
                holder.imgProduct2.setImageResource(R.drawable.sample_food);
            }
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
            Toast.makeText(context, "Xem chi tiết đơn #" + order.getOrderId(), Toast.LENGTH_SHORT).show();
            // TODO: Mở màn hình chi tiết đơn hàng
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
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


