package com.example.foodorder.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorder.Adapter.OrderHistoryAdapter;
import com.example.foodorder.R;
import com.example.foodorder.models.OrderPreview;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class OrderHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerOrderHistory;
    private OrderHistoryAdapter adapter;
    private List<OrderPreview> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        recyclerOrderHistory = findViewById(R.id.recyclerOrderHistory);
        recyclerOrderHistory.setLayoutManager(new LinearLayoutManager(this));

        // Tạo dữ liệu giả
        orderList = getMockOrders();

        adapter = new OrderHistoryAdapter(this, orderList);
        recyclerOrderHistory.setAdapter(adapter);
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

