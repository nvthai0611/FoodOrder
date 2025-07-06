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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private RecyclerView recyclerOrderHistory;
    private OrderHistoryAdapter adapter;
    private List<OrderPreview> orderList;

    private static final String TAG = "OrderHistoryActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        recyclerOrderHistory = findViewById(R.id.recyclerOrderHistory);
        recyclerOrderHistory.setLayoutManager(new LinearLayoutManager(this));

//        fetchOrdersByUser("user_123");

        orderList = getMockOrders();

        adapter = new OrderHistoryAdapter(this, orderList);
        recyclerOrderHistory.setAdapter(adapter);
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

    private List<OrderPreview> getMockOrders() {
        List<OrderPreview> list = new ArrayList<>();

        list.add(new OrderPreview(
                "10001",
                "02/07/2025 - 10:15",
                "Đã giao",
                235000,
                Arrays.asList("sample_food", "sample_drink", "sample_dessert")
        ));

        list.add(new OrderPreview(
                "10002",
                "01/07/2025 - 19:50",
                "Đang giao",
                187000,
                Arrays.asList("sample_food", "sample_drink")
        ));

        list.add(new OrderPreview(
                "10002",
                "01/07/2025 - 19:50",
                "Đang giao",
                187000,
                Arrays.asList("sample_food", "sample_drink")
        ));

        list.add(new OrderPreview(
                "10002",
                "01/07/2025 - 19:50",
                "Đang giao",
                187000,
                Arrays.asList("sample_food", "sample_drink")
        ));

        list.add(new OrderPreview(
                "10002",
                "01/07/2025 - 19:50",
                "Đang giao",
                187000,
                Arrays.asList("sample_food", "sample_drink")
        ));

        return list;
    }
}
