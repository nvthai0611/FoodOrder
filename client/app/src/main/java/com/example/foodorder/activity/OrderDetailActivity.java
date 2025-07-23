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

    import com.bumptech.glide.Glide;
    import com.example.foodorder.R;
    import com.example.foodorder.base.BaseActivity;
    import com.example.foodorder.models.Food;
    import com.example.foodorder.models.OrderDetail;
    import com.example.foodorder.models.OrderItem;
    import com.example.foodorder.network.ApiClient;
    import com.example.foodorder.network.OrderService;

    import java.text.NumberFormat;
    import java.text.SimpleDateFormat;
    import java.util.Date;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Locale;
    import java.util.Map;

    import retrofit2.Call;
    import retrofit2.Callback;
    import retrofit2.Response;

    public class OrderDetailActivity extends BaseActivity {

        private static final String TAG = "OrderDetailActivity";
        private static final String BASE_IMAGE_URL = "https://yourdomain.com/images/"; // cập nhật đúng domain ảnh của bạn

        private TextView tvOrderBy, tvOrderLocation, tvOrderStatus;
        private TextView tvScheduledTime, tvNote, tvTotalPrice;
        private LinearLayout orderItemsContainer;
        private Button btnCancelOrder;
        private ImageButton btnBack;

        private OrderDetail currentOrder;

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

            btnCancelOrder.setOnClickListener(v -> {
                if (currentOrder != null && "pending".equalsIgnoreCase(currentOrder.getStatus())) {
                    cancelOrder(currentOrder.get_id());
                }
            });

        }

        private void cancelOrder(String orderId) {
            OrderService service = ApiClient.getClient().create(OrderService.class);

            Map<String, String> body = new HashMap<>();
            body.put("status", "canceled");

            Call<OrderDetail> call = service.updateOrderStatus(orderId, body);

            call.enqueue(new Callback<OrderDetail>() {
                @Override
                public void onResponse(Call<OrderDetail> call, Response<OrderDetail> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(OrderDetailActivity.this, "Đã hủy đơn hàng", Toast.LENGTH_SHORT).show();
                        currentOrder = response.body();
                        populateOrderDetail(currentOrder); // cập nhật lại giao diện
                    } else {
                        Toast.makeText(OrderDetailActivity.this, "Hủy đơn thất bại", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<OrderDetail> call, Throwable t) {
                    Toast.makeText(OrderDetailActivity.this, "Lỗi khi kết nối đến server", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Lỗi hủy đơn", t);
                }
            });
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
            tvOrderBy.setText("Người đặt: " + order.getUser_id());
            tvOrderLocation.setText("Thời gian tạo: " + formatDate(order.getCreated_at()));
            tvOrderStatus.setText("Trạng thái: " + convertStatus(order.getStatus()));
            tvScheduledTime.setText("Thời gian giao: " + formatDate(order.getScheduled_time()));
            tvNote.setText("Ghi chú: " + (order.getNote() == null || order.getNote().isEmpty() ? "Không có" : order.getNote()));
            tvTotalPrice.setText("Tổng tiền: " + formatMoney(order.getTotal_price()));

            this.currentOrder = order;

            // Kích hoạt nút hủy nếu đơn hàng đang chờ xác nhận
            if ("pending".equalsIgnoreCase(order.getStatus())) {
                btnCancelOrder.setEnabled(true);
                btnCancelOrder.setAlpha(1.0f); // hiện rõ
            } else {
                btnCancelOrder.setEnabled(false);
                btnCancelOrder.setAlpha(0.5f); // làm mờ
            }

            orderItemsContainer.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from(this);

            List<OrderItem> items = order.getItems();

            for (OrderItem item : items) {
                View itemView = inflater.inflate(R.layout.item_order_detail, orderItemsContainer, false);

                TextView tvFoodName = itemView.findViewById(R.id.tvFoodName);
                TextView tvQuantityPrice = itemView.findViewById(R.id.tvQuantityPrice);
                ImageView imgFood = itemView.findViewById(R.id.imgFood);
                Button feedback = itemView.findViewById(R.id.feedback);

                Food food = item.getFood_id();
                if (food != null) {
                    tvFoodName.setText(food.getName());
                    tvQuantityPrice.setText("x" + item.getQuantity() + " • " + formatMoney(item.getPrice() * item.getQuantity()));
                    // Load ảnh từ URL (hoặc tên file ghép BASE_IMAGE_URL)
                    String imageUrl = food.getImageUrl();
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        if (!imageUrl.startsWith("http")) {
                            imageUrl = BASE_IMAGE_URL + imageUrl;
                        }

                        Glide.with(this)
                                .load(imageUrl)
                                .placeholder(R.drawable.sample_food)
                                .error(R.drawable.sample_food)
                                .into(imgFood);
                    } else {
                        imgFood.setImageResource(R.drawable.sample_food);
                    }

                    feedback.setOnClickListener(v -> {
                        Intent intent = new Intent(OrderDetailActivity.this, ReviewFoodActivity.class);
                        intent.putExtra("food_id", food.getId());
                        intent.putExtra("food_quantity", item.getQuantity());
                        intent.putExtra("food_name", food.getName());
                        startActivity(intent);
                    });
                } else {
                    tvFoodName.setText("Không xác định");
                    tvQuantityPrice.setText("x" + item.getQuantity());
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

        private String formatMoney(double money) {
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
