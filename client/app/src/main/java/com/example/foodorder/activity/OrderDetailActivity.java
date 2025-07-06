//<<<<<<< Updated upstream
////package com.example.foodorder.activity;
////
////import android.os.Bundle;
////import android.util.Log;
////import android.view.View;
////import android.widget.Button;
////import android.widget.ImageButton;
////import android.widget.LinearLayout;
////import android.widget.TextView;
////import android.widget.Toast;
////
////import androidx.appcompat.app.AppCompatActivity;
////
////import com.bumptech.glide.Glide;
////import com.example.foodorder.R;
////import com.example.foodorder.models.OrderDetail;
////import com.example.foodorder.models.OrderItem;
////import com.example.foodorder.network.ApiClient;
////import com.example.foodorder.network.OrderService;
////
////import java.util.List;
////
////import de.hdodenhof.circleimageview.CircleImageView;
////import retrofit2.Call;
////import retrofit2.Callback;
////import retrofit2.Response;
////
////public class OrderDetailActivity extends AppCompatActivity {
////
////    private ImageButton btnBack;
////    private TextView tvOrderBy, tvOrderLocation, tvOrderStatus;
////    private LinearLayout orderItemsContainer;
////    private Button btnCancelOrder;
////
////    private static final String TAG = "OrderDetailActivity";
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.order_detail);
////
////        btnBack = findViewById(R.id.btnBack);
////        tvOrderBy = findViewById(R.id.tvOrderBy);
////        tvOrderLocation = findViewById(R.id.tvOrderLocation);
////        tvOrderStatus = findViewById(R.id.tvOrderStatus);
////        orderItemsContainer = findViewById(R.id.orderItemsContainer);
////        btnCancelOrder = findViewById(R.id.btnCancelOrder);
////
////        btnBack.setOnClickListener(v -> finish());
////
////        String orderId = getIntent().getStringExtra("orderId");
////        if (orderId != null) {
////            fetchOrderDetail(orderId);
////        } else {
////            Toast.makeText(this, "Không tìm thấy đơn hàng", Toast.LENGTH_SHORT).show();
////            finish();
////        }
////    }
////
////    private void fetchOrderDetail(String orderId) {
////        OrderService service = ApiClient.getClient().create(OrderService.class);
////        Call<OrderDetail> call = service.getOrderDetail(orderId);
////
////        call.enqueue(new Callback<OrderDetail>() {
////            @Override
////            public void onResponse(Call<OrderDetail> call, Response<OrderDetail> response) {
////                if (response.isSuccessful() && response.body() != null) {
////                    populateOrderDetail(response.body());
////                } else {
////                    Toast.makeText(OrderDetailActivity.this, "Lỗi lấy chi tiết đơn hàng", Toast.LENGTH_SHORT).show();
////                }
////            }
////
////            @Override
////            public void onFailure(Call<OrderDetail> call, Throwable t) {
////                Log.e(TAG, "API fail: " + t.getMessage());
////                Toast.makeText(OrderDetailActivity.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
////            }
////        });
////    }
////
////    private void populateOrderDetail(OrderDetail order) {
////        tvOrderBy.setText("Người đặt: " + order.getUserName());
////        tvOrderLocation.setText("Địa chỉ: " + order.getAddress());
////        tvOrderStatus.setText("Trạng thái: " + order.getStatus());
////
////        orderItemsContainer.removeAllViews();
////
////        for (OrderItem item : order.getItems()) {
////            View itemView = getLayoutInflater().inflate(R.layout.item_order_detail, orderItemsContainer, false);
////
////            TextView tvName = itemView.findViewById(R.id.tvFoodName);
////            TextView tvQty = itemView.findViewById(R.id.tvQuantity);
////            TextView tvPrice = itemView.findViewById(R.id.tvPrice);
////            CircleImageView imgFood = itemView.findViewById(R.id.imgFood);
////
////            tvName.setText(item.getName());
////            tvQty.setText("x" + item.getQuantity());
////            tvPrice.setText(item.getPrice() + "đ");
////
////            Glide.with(this).load(item.getImageUrl()).into(imgFood);
////
////            orderItemsContainer.addView(itemView);
////        }
////    }
////}
//=======
//package com.example.foodorder.activity;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.bumptech.glide.Glide;
//import com.example.foodorder.models.Food;
//import com.example.foodorder.models.Order;
//import com.example.foodorder.models.OrderItem;
//import com.example.foodorder.R;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class OrderDetailActivity extends AppCompatActivity {
//
//    private LinearLayout orderItemsContainer;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.order_detail);
//
//        TextView tvOrderBy = findViewById(R.id.tvOrderBy);
//        TextView tvOrderLocation = findViewById(R.id.tvOrderLocation);
//        TextView tvOrderStatus = findViewById(R.id.tvOrderStatus);
//        Button btnCancel = findViewById(R.id.btnCancelOrder);
//        ImageButton btnBack = findViewById(R.id.btnBack);
//        orderItemsContainer = findViewById(R.id.orderItemsContainer);
//
//        Order order = new Order(
//                1,
//                101,
//                135000,
//                "pending",
//                "Không hành",
//                "2025-06-30 12:30",
//                "2025-06-30 10:15"
//        );
//
//        String userName = "Nguyễn Văn A";
//        String address = "123 Lý Thường Kiệt, Hà Nội";
//
//        tvOrderBy.setText("Người đặt: " + userName);
//        tvOrderLocation.setText("Địa chỉ: " + address);
//        tvOrderStatus.setText("Trạng thái: " + convertStatus(order.getStatus()));
//
//        List<OrderItem> items = new ArrayList<>();
//
////        items.add(new OrderItem(1, order.getId(), 1, 2, 70_000,
////                new Food(1, "Double Decker", "Beef Burger", "https://res.cloudinary.com/dickb1q09/image/upload/v1750522475/HoaLacRent/aezn55ktqcj3qa3zebjl.jpg", "Demo", 35.0, true, "", 3)));
////
////        items.add(new OrderItem(2, order.getId(), 2, 1, 30_000,
////                new Food(2, "Smoke House", "Chicken Burger", "https://res.cloudinary.com/dickb1q09/image/upload/v1750522475/HoaLacRent/aezn55ktqcj3qa3zebjl.jpg", "Demo", 30.0, true, "", 1)));
////
////        items.add(new OrderItem(3, order.getId(), 3, 3, 45_000,
////                new Food(3, "Vegetable Salad", "Lettuce and Tomatoes", "https://res.cloudinary.com/dickb1q09/image/upload/v1750522475/HoaLacRent/aezn55ktqcj3qa3zebjl.jpg", "Demo", 15.0, true, "", 2)));
//
//        LayoutInflater inflater = LayoutInflater.from(this);
//        for (OrderItem item : items) {
//            View view = inflater.inflate(R.layout.order_item, orderItemsContainer, false);
//
//            ImageView img = view.findViewById(R.id.imgFoodItem);
//            TextView tvName = view.findViewById(R.id.tvFoodName);
//            TextView tvQty = view.findViewById(R.id.tvQuantity);
//
//            Glide.with(this)
//                    .load(item.getFood().getImageUrl()) // lấy URL ảnh
//                    .placeholder(R.drawable.ic_launcher_background)
//                    .into(img);
//
//            tvName.setText(item.getFood().getName());
//            tvQty.setText("Số lượng: " + item.getQuantity());
//
//            orderItemsContainer.addView(view);
//        }
//
//        btnBack.setOnClickListener(v -> finish());
//
//        btnCancel.setOnClickListener(v -> {
//            Toast.makeText(this, "Đã yêu cầu hủy đơn hàng", Toast.LENGTH_SHORT).show();
//        });
//    }
//
//    private String convertStatus(String status) {
//        switch (status) {
//            case "pending": return "Chờ xác nhận";
//            case "preparing": return "Đang chuẩn bị";
//            case "done": return "Hoàn thành";
//            case "canceled": return "Đã hủy";
//            default: return "Không rõ";
//        }
//    }
//}
//>>>>>>> Stashed changes
