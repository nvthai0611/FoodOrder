package com.example.foodorder.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.foodorder.models.Food;
import com.example.foodorder.models.Order;
import com.example.foodorder.models.OrderItem;
import com.example.foodorder.R;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {

    private LinearLayout orderItemsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_detail);

        TextView tvOrderBy = findViewById(R.id.tvOrderBy);
        TextView tvOrderLocation = findViewById(R.id.tvOrderLocation);
        TextView tvOrderStatus = findViewById(R.id.tvOrderStatus);
        Button btnCancel = findViewById(R.id.btnCancelOrder);
        ImageButton btnBack = findViewById(R.id.btnBack);
        orderItemsContainer = findViewById(R.id.orderItemsContainer);

        Order order = new Order(
                "1",
                "101",
                135000,     
                "pending",
                "Không hành", 
                "2025-06-30 12:30", 
                "2025-06-30 10:15" 
        );

        String userName = "Nguyễn Văn A";
        String address = "123 Lý Thường Kiệt, Hà Nội";

        tvOrderBy.setText("Người đặt: " + userName);
        tvOrderLocation.setText("Địa chỉ: " + address);
        tvOrderStatus.setText("Trạng thái: " + convertStatus(order.getStatus()));

        List<OrderItem> items = new ArrayList<>();

//        items.add(new OrderItem(1, order.getId(), 1, 2, 70_000,
//                new Food(1, "Double Decker", "Beef Burger", "https://res.cloudinary.com/dickb1q09/image/upload/v1750522475/HoaLacRent/aezn55ktqcj3qa3zebjl.jpg", "Demo", 35.0, true, "", 3)));
//
//        items.add(new OrderItem(2, order.getId(), 2, 1, 30_000,
//                new Food(2, "Smoke House", "Chicken Burger", "https://res.cloudinary.com/dickb1q09/image/upload/v1750522475/HoaLacRent/aezn55ktqcj3qa3zebjl.jpg", "Demo", 30.0, true, "", 1)));
//
//        items.add(new OrderItem(3, order.getId(), 3, 3, 45_000,
//                new Food(3, "Vegetable Salad", "Lettuce and Tomatoes", "https://res.cloudinary.com/dickb1q09/image/upload/v1750522475/HoaLacRent/aezn55ktqcj3qa3zebjl.jpg", "Demo", 15.0, true, "", 2)));

        LayoutInflater inflater = LayoutInflater.from(this);
        for (OrderItem item : items) {
            View view = inflater.inflate(R.layout.order_item, orderItemsContainer, false);

            ImageView img = view.findViewById(R.id.imgFoodItem);
            TextView tvName = view.findViewById(R.id.tvFoodName);
            TextView tvQty = view.findViewById(R.id.tvQuantity);

            Glide.with(this)
                    .load(item.getFood().getImageUrl()) // lấy URL ảnh
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(img);

            tvName.setText(item.getFood().getName());
            tvQty.setText("Số lượng: " + item.getQuantity());

            orderItemsContainer.addView(view);
        }

        btnBack.setOnClickListener(v -> finish());

        btnCancel.setOnClickListener(v -> {
            Toast.makeText(this, "Đã yêu cầu hủy đơn hàng", Toast.LENGTH_SHORT).show();
        });
    }

    private String convertStatus(String status) {
        switch (status) {
            case "pending": return "Chờ xác nhận";
            case "preparing": return "Đang chuẩn bị";
            case "done": return "Hoàn thành";
            case "canceled": return "Đã hủy";
            default: return "Không rõ";
        }
    }
}
