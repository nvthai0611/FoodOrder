package com.example.foodorder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorder.Adapter.OrderHistoryAdapter;
import com.example.foodorder.R;
import com.example.foodorder.base.BaseActivity;
import com.example.foodorder.models.Order;
import com.example.foodorder.models.OrderItem;
import com.example.foodorder.models.Food;
import com.example.foodorder.network.ApiClient;
import com.example.foodorder.network.OrderService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryActivity extends BaseActivity {

    private RecyclerView recyclerOrderHistory;
    private OrderHistoryAdapter adapter;
    private List<Order> originalOrderList;

    private static final String TAG = "OrderHistoryActivity";
    private static final String USER_ID = "64b9b842f5b123456789abcd";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        recyclerOrderHistory = findViewById(R.id.recyclerOrderHistory);
        recyclerOrderHistory.setLayoutManager(new LinearLayoutManager(this));

        EditText etSearch = findViewById(R.id.etSearch);
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            String keyword = etSearch.getText().toString().trim();
            if (adapter != null) {
                adapter.filterByOrderId(keyword);
            }
            return true;
        });

        fetchOrdersByUser(USER_ID);
    }

    private void fetchOrdersByUser(String userId) {
        OrderService orderService = ApiClient.getClient().create(OrderService.class);
        Call<List<Order>> call = orderService.getOrdersByUser(userId);

        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    originalOrderList = response.body();
                    adapter = new OrderHistoryAdapter(OrderHistoryActivity.this, originalOrderList, order -> {
                        if (order.getItems() != null && !order.getItems().isEmpty()) {
                            OrderItem firstItem = order.getItems().get(0);
                            Food food = firstItem.getFood_id();

                            if (food != null) {
                                Intent intent = new Intent(OrderHistoryActivity.this, HomeActivity.class);
                                intent.putExtra("food_id", food.getId());
                                intent.putExtra("food_name", food.getName());
                                intent.putExtra("food_price", food.getPrice());
                                intent.putExtra("food_image", food.getImageUrl()); // Nếu có ảnh
                                startActivity(intent);
                            }
                        }
                    });
                    recyclerOrderHistory.setAdapter(adapter);
                    Log.d(TAG, "Đã load " + originalOrderList.size() + " đơn hàng của user " + userId);
                } else {
                    Toast.makeText(OrderHistoryActivity.this, "Không thể lấy đơn hàng", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Lỗi phản hồi: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Toast.makeText(OrderHistoryActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e(TAG, "Lỗi API", t);
            }
        });
    }
}
