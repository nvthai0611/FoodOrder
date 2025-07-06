//package com.example.foodorder.activity;
//
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageButton;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.bumptech.glide.Glide;
//import com.example.foodorder.R;
//import com.example.foodorder.models.OrderDetail;
//import com.example.foodorder.models.OrderItem;
//import com.example.foodorder.network.ApiClient;
//import com.example.foodorder.network.OrderService;
//
//import java.util.List;
//
//import de.hdodenhof.circleimageview.CircleImageView;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class OrderDetailActivity extends AppCompatActivity {
//
//    private ImageButton btnBack;
//    private TextView tvOrderBy, tvOrderLocation, tvOrderStatus;
//    private LinearLayout orderItemsContainer;
//    private Button btnCancelOrder;
//
//    private static final String TAG = "OrderDetailActivity";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.order_detail);
//
//        btnBack = findViewById(R.id.btnBack);
//        tvOrderBy = findViewById(R.id.tvOrderBy);
//        tvOrderLocation = findViewById(R.id.tvOrderLocation);
//        tvOrderStatus = findViewById(R.id.tvOrderStatus);
//        orderItemsContainer = findViewById(R.id.orderItemsContainer);
//        btnCancelOrder = findViewById(R.id.btnCancelOrder);
//
//        btnBack.setOnClickListener(v -> finish());
//
//        String orderId = getIntent().getStringExtra("orderId");
//        if (orderId != null) {
//            fetchOrderDetail(orderId);
//        } else {
//            Toast.makeText(this, "Không tìm thấy đơn hàng", Toast.LENGTH_SHORT).show();
//            finish();
//        }
//    }
//
//    private void fetchOrderDetail(String orderId) {
//        OrderService service = ApiClient.getClient().create(OrderService.class);
//        Call<OrderDetail> call = service.getOrderDetail(orderId);
//
//        call.enqueue(new Callback<OrderDetail>() {
//            @Override
//            public void onResponse(Call<OrderDetail> call, Response<OrderDetail> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    populateOrderDetail(response.body());
//                } else {
//                    Toast.makeText(OrderDetailActivity.this, "Lỗi lấy chi tiết đơn hàng", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<OrderDetail> call, Throwable t) {
//                Log.e(TAG, "API fail: " + t.getMessage());
//                Toast.makeText(OrderDetailActivity.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void populateOrderDetail(OrderDetail order) {
//        tvOrderBy.setText("Người đặt: " + order.getUserName());
//        tvOrderLocation.setText("Địa chỉ: " + order.getAddress());
//        tvOrderStatus.setText("Trạng thái: " + order.getStatus());
//
//        orderItemsContainer.removeAllViews();
//
//        for (OrderItem item : order.getItems()) {
//            View itemView = getLayoutInflater().inflate(R.layout.item_order_detail, orderItemsContainer, false);
//
//            TextView tvName = itemView.findViewById(R.id.tvFoodName);
//            TextView tvQty = itemView.findViewById(R.id.tvQuantity);
//            TextView tvPrice = itemView.findViewById(R.id.tvPrice);
//            CircleImageView imgFood = itemView.findViewById(R.id.imgFood);
//
//            tvName.setText(item.getName());
//            tvQty.setText("x" + item.getQuantity());
//            tvPrice.setText(item.getPrice() + "đ");
//
//            Glide.with(this).load(item.getImageUrl()).into(imgFood);
//
//            orderItemsContainer.addView(itemView);
//        }
//    }
//}
