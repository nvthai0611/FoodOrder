package com.example.foodorder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.foodorder.R;
import com.example.foodorder.base.BaseActivity;
import com.example.foodorder.models.OrderDetail;
import com.example.foodorder.models.OrderItem;
import com.example.foodorder.network.ApiClient;
import com.example.foodorder.network.OrderService;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends BaseActivity {

    private static final String TAG = "OrderDetailActivity";

    private TextView tvOrderBy, tvOrderLocation, tvOrderStatus;
    private TextView tvScheduledTime, tvNote, tvTotalPrice;
    private LinearLayout orderItemsContainer;
    private Button btnCancelOrder;
    private ImageButton btnBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        String orderId = getIntent().getStringExtra("order_id");
        if (orderId == null) {
            Toast.makeText(this, "Không tìm thấy mã đơn hàng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initView();
        loadOrderDetail(orderId);
    }

    private void initView() {
        tvOrderBy = findViewById(R.id.tvOrderBy);
        tvOrderLocation = findViewById(R.id.tvOrderLocation);
        tvOrderStatus = findViewById(R.id.tvOrderStatus);
        tvScheduledTime = findViewById(R.id.tvScheduledTime);
        tvNote = findViewById(R.id.tvNote);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        orderItemsContainer = findViewById(R.id.orderItemsContainer);
        btnCancelOrder = findViewById(R.id.btnCancelOrder);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        btnCancelOrder.setOnClickListener(v ->
                Toast.makeText(this, "Tính năng hủy đơn đang phát triển", Toast.LENGTH_SHORT).show());
    }

    private void loadOrderDetail(String orderId) {
        OrderService service = ApiClient.getClient().create(OrderService.class);
        Call<OrderDetail> call = service.getOrderDetail(orderId);

        call.enqueue(new Callback<OrderDetail>() {
            @Override
            public void onResponse(Call<OrderDetail> call, Response<OrderDetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    populateOrderDetail(response.body());
                } else {
                    Toast.makeText(OrderDetailActivity.this, "Không thể tải chi tiết đơn hàng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OrderDetail> call, Throwable t) {
                Toast.makeText(OrderDetailActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e(TAG, "Lỗi khi load đơn hàng", t);
            }
        });
    }

    private void populateOrderDetail(OrderDetail order) {
        tvOrderBy.setText("Người đặt: " + order.user_id);
        tvOrderLocation.setText("Thời gian tạo: " + formatDate(order.created_at));
        tvOrderStatus.setText("Trạng thái: " + convertStatus(order.status));
        tvScheduledTime.setText("Thời gian giao: " + formatDate(order.scheduled_time));
        tvNote.setText("Ghi chú: " + (order.note.isEmpty() ? "Không có" : order.note));
        tvTotalPrice.setText("Tổng tiền: " + formatMoney(order.total_price));

        orderItemsContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        for (int i = 0; i < order.items.size(); i++) {
            OrderItem item = order.items.get(i);
            View itemView = inflater.inflate(R.layout.item_order_detail, orderItemsContainer, false);

            TextView tvFoodName = itemView.findViewById(R.id.tvFoodName);
            TextView tvQuantityPrice = itemView.findViewById(R.id.tvQuantityPrice);
            ImageView imgFood = itemView.findViewById(R.id.imgFood);
            Button feedback = itemView.findViewById(R.id.feedback);
            feedback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(OrderDetailActivity.this, ReviewFoodActivity.class); // hoặc tên Activity bạn dùng để gửi đánh giá

                    intent.putExtra("food_id", item.food_id);  // truyền ID món ăn
                    intent.putExtra("food_quantity", item.quantity); // truyền số lượng đã đặt (nếu cần)
                    intent.putExtra("food_name", item.name); // tuỳ ý truyền thêm

                    startActivity(intent);
                }
            });
            tvFoodName.setText(item.name);
            tvQuantityPrice.setText("x" + item.quantity + " • " + formatMoney(item.price * item.quantity));

            // Hiển thị ảnh theo thứ tự trong food_images (nếu có)
            if (order.food_images != null && i < order.food_images.size()) {
                int resId = getResources().getIdentifier(order.food_images.get(i), "drawable", getPackageName());
                if (resId != 0) {
                    imgFood.setImageResource(resId);
                } else {
                    imgFood.setImageResource(R.drawable.sample_food);
                }
            } else {
                imgFood.setImageResource(R.drawable.sample_food);
            }

            orderItemsContainer.addView(itemView);
        }
    }

    private String formatDate(String isoTime) {
        try {
            SimpleDateFormat iso = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            Date date = iso.parse(isoTime);
            SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.getDefault());
            return fmt.format(date);
        } catch (Exception e) {
            return isoTime;
        }
    }

    private String formatMoney(int money) {
        return NumberFormat.getNumberInstance(Locale.getDefault()).format(money) + "đ";
    }

    private String convertStatus(String status) {
        switch (status) {
            case "pending": return "Chờ xác nhận";
            case "preparing": return "Đang chuẩn bị";
            case "done": return "Đã giao";
            default: return status;
        }
    }
}
