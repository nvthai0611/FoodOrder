package com.example.foodorder.activity;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorder.Adapter.CartAdapter;
import com.example.foodorder.R;
import com.example.foodorder.models.Cart;
import com.example.foodorder.utils.Routing;

public class CheckoutActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private Cart cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_checkout);

        String userId = getSharedPreferences("MyAppPrefs", MODE_PRIVATE).getString("uId", null);

        if (userId == null) Routing.redirect(this, LoginActivity.class);

        recyclerView = findViewById(R.id.cartRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CartAdapter(cart);
        recyclerView.setAdapter(adapter);

        cart = (Cart) getIntent().getSerializableExtra("cart");

        if (cart != null) Routing.redirect(this, HomeActivity.class);

        Button btnPrepaid = findViewById(R.id.btnPrepaid);
        Button btnCOD = findViewById(R.id.btnPayOnDelivery);

        btnPrepaid.setOnClickListener(v -> {
            ppCheckout();
        });

        btnCOD.setOnClickListener(v -> {
            codCheckout();
        });
    }

    private void ppCheckout() {
        //TODO: Implement banking API
    }

    private void codCheckout() {

    }

    private void createOrder() {
    }
}