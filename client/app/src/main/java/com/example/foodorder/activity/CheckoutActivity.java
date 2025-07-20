package com.example.foodorder.activity;

import static android.widget.Toast.LENGTH_SHORT;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorder.Adapter.CartAdapter;
import com.example.foodorder.R;
import com.example.foodorder.models.Cart;
import com.example.foodorder.utils.RoutingUtils;

public class CheckoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_checkout);

        String userId = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                .getString("uId", null);
        String fullName = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                .getString("uName", null);

        if (userId == null) {
            RoutingUtils.redirect(this, LoginActivity.class, true);
            return;
        }

        Cart cart = (Cart) getIntent().getSerializableExtra("cart");
        String totalString = getIntent().getStringExtra("total");

        TextView total = findViewById(R.id.total);
        TextView name = findViewById(R.id.billingName);


        total.setText("Tổng cộng: " + totalString);
        name.setText(fullName);

        if (cart == null) {
            RoutingUtils.redirect(this, HomeActivity.class, true);
            return;
        }

        RecyclerView recyclerView = findViewById(R.id.cartRecyclerView);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        CartAdapter adapter = new CartAdapter(cart);
        recyclerView.setAdapter(adapter);

        Button btnPrepaid = findViewById(R.id.btnPrepaid);
        Button btnCOD = findViewById(R.id.btnPayOnDelivery);

        btnPrepaid.setOnClickListener(v -> ppCheckout());
        btnCOD.setOnClickListener(v -> codCheckout());
    }

    @Override
    protected void onResume() {
        super.onResume();

        TextView total = findViewById(R.id.total);
        String totalStr = getIntent().getStringExtra("total");
        if (totalStr != null) total.setText("Tổng cộng: " + totalStr);
    }

    private void ppCheckout() {
        //TODO: Implement banking API
        Toast.makeText(this, "Visa gone", LENGTH_SHORT).show();
    }

    private void codCheckout() {
        Toast.makeText(this, "Boomed", LENGTH_SHORT).show();
    }

    private void createOrder() {

    }
}