package com.example.foodorder.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorder.Adapter.OrderHistoryAdapter;
import com.example.foodorder.R;
import com.example.foodorder.base.BaseActivity;
import com.example.foodorder.models.OrderPreview;
import com.example.foodorder.network.ApiClient;
import com.example.foodorder.network.OrderService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private OrderHistoryAdapter adapter;
    private static final String TAG = "OrderHistoryActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        recyclerView = findViewById(R.id.recyclerOrderHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchOrdersByUser("user_123"); // 🔁 Thay bằng userId thật nếu có
    }

    private void fetchOrdersByUser(String userId) {
        OrderService orderService = ApiClient.getClient().create(OrderService.class);
        Call<List<OrderPreview>> call = orderService.getOrdersByUser(userId);

        call.enqueue(new Callback<List<OrderPreview>>() {
            @Override
            public void onResponse(Call<List<OrderPreview>> call, Response<List<OrderPreview>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<OrderPreview> orders = response.body();
                    adapter = new OrderHistoryAdapter(OrderHistoryActivity.this, orders);
                    recyclerView.setAdapter(adapter);
                    Log.d(TAG, "Đã load " + orders.size() + " đơn hàng");
                } else {
                    Toast.makeText(OrderHistoryActivity.this, "Không thể lấy đơn hàng", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Lỗi phản hồi: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<OrderPreview>> call, Throwable t) {
                Toast.makeText(OrderHistoryActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e(TAG, "Lỗi API", t);
            }
        });
    }
}
