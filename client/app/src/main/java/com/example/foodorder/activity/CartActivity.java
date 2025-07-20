package com.example.foodorder.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorder.Adapter.CartAdapter;
import com.example.foodorder.R;
import com.example.foodorder.base.BaseActivity;
import com.example.foodorder.models.Cart;
import com.example.foodorder.network.ApiClient;
import com.example.foodorder.network.CartService;
import com.example.foodorder.utils.RoutingUtils;

import java.io.IOException;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private Cart cart;
    private TextView totalPriceText;
    private CartService cartService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);

        cartService = ApiClient.getClient().create(CartService.class);
        totalPriceText = findViewById(R.id.totalPrice);
        recyclerView = findViewById(R.id.cartRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button btnCheckout = findViewById(R.id.btnCheckout);
        btnCheckout.setOnClickListener(v -> {
            checkout();
        });

        String userId = getCurrentUserId();
        if (userId != null) {
            fetchCart(userId);
        } else {
            RoutingUtils.redirect(this, LoginActivity.class, true);
        }
    }

    private void checkout() {
        Bundle extras = new Bundle();
        extras.putSerializable("cart", cart);
        extras.putString("total", totalPriceText.getText().toString());
        RoutingUtils.redirect(this, CheckoutActivity.class, extras, false);
    }

    private void fetchCart(String userId) {
        Call<Cart> call = cartService.getCart(userId);
        call.enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(@NonNull Call<Cart> call, @NonNull Response<Cart> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cart = response.body();
                    adapter = new CartAdapter(cart);
                    recyclerView.setAdapter(adapter);
                    totalPriceText.setText(String.format(Locale.US, "$%.2f", calculateTotal(cart)));
                } else {
                    try {
                        String errorMsg = response.errorBody().string();
                        Log.e("CAR_ACT", errorMsg);
                        Toast.makeText(CartActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(CartActivity.this, "Không thể tải giỏ hàng", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Cart> call, @NonNull Throwable t) {
                Toast.makeText(CartActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("CAR_ACT", t.getMessage());
                RoutingUtils.redirect(CartActivity.this, HomeActivity.class, true);
            }
        });
    }

    private Double calculateTotal(Cart cart) {
        final Double[] totalAmount = new Double[]{-1d};
        cart.getCartItems().forEach(item -> {
            totalAmount[0] += item.getPrice() * item.getQuantity();
        });
        return totalAmount[0];
    }

    private String getCurrentUserId() {
        return getSharedPreferences("MyAppPrefs", MODE_PRIVATE).getString("uId", null);
    }
}
