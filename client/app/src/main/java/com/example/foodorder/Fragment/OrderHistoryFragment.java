package com.example.foodorder.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorder.Adapter.OrderHistoryAdapter;
import com.example.foodorder.R;
import com.example.foodorder.activity.OrderDetailActivity;
import com.example.foodorder.models.Order;
import com.example.foodorder.network.ApiClient;
import com.example.foodorder.network.OrderService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryFragment extends Fragment {

    private RecyclerView recyclerOrderHistory;
    private EditText etSearch;
    private OrderHistoryAdapter adapter;
    private List<Order> originalOrderList;

    private static final String USER_ID = "64b9b842f5b123456789abcd"; // Có thể truyền từ ngoài nếu cần

    public OrderHistoryFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);

        recyclerOrderHistory = view.findViewById(R.id.recyclerOrderHistory);
        etSearch = view.findViewById(R.id.etSearch);

        recyclerOrderHistory.setLayoutManager(new LinearLayoutManager(getContext()));

        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (adapter != null) {
                adapter.filterByOrderId(etSearch.getText().toString().trim());
            }
            return true;
        });

        fetchOrdersByUser(USER_ID);

        return view;
    }

    private void fetchOrdersByUser(String userId) {
        OrderService orderService = ApiClient.getClient().create(OrderService.class);
        Call<List<Order>> call = orderService.getOrdersByUser(userId);

        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    originalOrderList = response.body();
                    adapter = new OrderHistoryAdapter(getContext(), originalOrderList, order -> {
                        // Khi người dùng bấm "Xem chi tiết", điều hướng sang OrderDetailActivity
                        Intent intent = new Intent(getContext(), OrderDetailActivity.class);
                        intent.putExtra("order_id", order.get_id());
                        startActivity(intent);
                    });

                    recyclerOrderHistory.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Không thể lấy đơn hàng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
