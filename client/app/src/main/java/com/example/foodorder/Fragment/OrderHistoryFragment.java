package com.example.foodorder.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.example.foodorder.activity.MainActivity;
import com.example.foodorder.activity.OrderDetailActivity;
import com.example.foodorder.activity.OrderHistoryActivity;
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


    public OrderHistoryFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);
        // Sử dụng getActivity().getSharedPreferences trong Fragment
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String uId = sharedPreferences.getString("uId", null);
        String uName = sharedPreferences.getString("uName", null);
        String uEmail = sharedPreferences.getString("uEmail", null);

        if (uId == null && uName == null && uEmail == null) {
            // Nếu đã đăng nhập thì chuyển về MainActivity
            Intent intent = new Intent(requireActivity(), MainActivity.class);
            startActivity(intent);
            requireActivity().finish(); // dùng requireActivity() để gọi finish()
            return view; // vẫn cần return view ở cuối
        }
        recyclerOrderHistory = view.findViewById(R.id.recyclerOrderHistory);
        etSearch = view.findViewById(R.id.etSearch);

        recyclerOrderHistory.setLayoutManager(new LinearLayoutManager(getContext()));

        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (adapter != null) {
                adapter.filterByOrderId(etSearch.getText().toString().trim());
            }
            return true;
        });

        fetchOrdersByUser(uId);

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
                Log.e("fetchOrders", "Lỗi: " + t.getMessage());
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
