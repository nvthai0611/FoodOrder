package com.example.foodorder.activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.DisplayCutoutCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodorder.Adapter.CartAdapter;
import com.example.foodorder.R;
import com.example.foodorder.models.Cart;
import com.example.foodorder.models.CartItem;
import com.example.foodorder.models.Order;
import com.example.foodorder.models.PaymentInfo;
import com.example.foodorder.network.ApiClient;
import com.example.foodorder.network.OrderService;
import com.example.foodorder.network.PaymentService;
import com.example.foodorder.response.PaymentCheckResponse;
import com.example.foodorder.utils.RoutingUtils;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutActivity extends AppCompatActivity {
    private static final int BASE_MARGIN = 16;

    private Cart cart;
    private String userId;
    private double totalPrice;
    private boolean isPaymentSuccess = false;
    private Handler handler = new Handler();
    private Runnable paymentCheckRunnable;

    private OrderService orderService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_checkout);

        View checkoutLayout = findViewById(R.id.checkoutLayout);
        TextView checkoutTitle = findViewById(R.id.checkoutTitle);

        ViewCompat.setOnApplyWindowInsetsListener(checkoutLayout, (v, insets) -> {
            DisplayCutoutCompat cutout = insets.getDisplayCutout();
            if (cutout != null) {
                int topInset = cutout.getSafeInsetTop();

                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) checkoutTitle.getLayoutParams();
                params.topMargin = topInset + dpToPx(BASE_MARGIN);
                checkoutTitle.setLayoutParams(params);
            }
            return insets;
        });


        orderService = ApiClient.getClient().create(OrderService.class);
        userId = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                .getString("uId", null);
        String fullName = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                .getString("uName", null);

        if (userId == null) {
            RoutingUtils.redirect(this, LoginActivity.class, RoutingUtils.NO_EXTRAS, RoutingUtils.ACTIVITY_FINISH);
            return;
        }

        cart = (Cart) getIntent().getSerializableExtra("cart");
        if (cart == null) {
            RoutingUtils.redirect(this, HomeActivity.class, RoutingUtils.NO_EXTRAS, RoutingUtils.ACTIVITY_FINISH);
            return;
        }

        totalPrice = calculateTotalPrice(cart);

        TextView total = findViewById(R.id.total);
        TextView name = findViewById(R.id.billingName);

        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
        String formattedPrice = formatter.format(totalPrice);
        total.setText("Tổng tiền: " + formattedPrice);
        name.setText(fullName);

        RecyclerView recyclerView = findViewById(R.id.cartRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new CartAdapter(cart));

        Button btnPrepaid = findViewById(R.id.btnPrepaid);
        Button btnCOD = findViewById(R.id.btnPayOnDelivery);

        btnPrepaid.setOnClickListener(v -> ppCheckout());
        btnCOD.setOnClickListener(v -> codCheckout());
    }

    private double calculateTotalPrice(Cart cart) {
        double total = 0;
        for (CartItem item : cart.getCartItems()) {
            total += item.getPrice() * item.getQuantity();
        }
        return Math.round(total * 100.0) / 100.0; // làm tròn 2 chữ số thập phân
    }

    private void ppCheckout() {
        Map<String, String> body = new HashMap<>();
        body.put("userId", userId);
        body.put("totalPrice", String.format(Locale.US, "%.2f", totalPrice));

        PaymentService service = ApiClient.getClient().create(PaymentService.class);
        service.createPaymentInfo(body).enqueue(new Callback<PaymentInfo>() {
            @Override
            public void onResponse(@NonNull Call<PaymentInfo> call, @NonNull Response<PaymentInfo> response) {
                if (response.isSuccessful() && response.body() != null) {
                    showPaymentDialog(response.body());
                } else {
                    Toast.makeText(CheckoutActivity.this, "Không lấy được thông tin thanh toán", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<PaymentInfo> call, @NonNull Throwable t) {
                Toast.makeText(CheckoutActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showPaymentDialog(PaymentInfo info) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thanh toán");

        View view = getLayoutInflater().inflate(R.layout.dialog_qr_payment, null);
        builder.setView(view);

        TextView txtBankInfo = view.findViewById(R.id.txtBankInfo);
        TextView txtNote = view.findViewById(R.id.txtNote);
        ImageView imgQr = view.findViewById(R.id.imgQr);

        txtBankInfo.setText(
                "Ngân hàng: " + info.getBankName() +
                        "\nSố tài khoản: " + info.getAccountNumber() +
                        "\nChủ tài khoản: " + info.getAccountName() +
                        "\nSố tiền: " + info.getAmount()
        );

        txtNote.setText("Nội dung: " + info.getNote());

        Glide.with(this).load(info.getQrUrl()).into(imgQr);

        builder.setNegativeButton("Huỷ", null);
        builder.create().show();

        // Tự động kiểm tra trạng thái thanh toán mỗi 3 giây
        paymentCheckRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isPaymentSuccess) {
                    checkPaymentStatus(info.getNote(), String.format(Locale.US, "%.0f", info.getAmount()));
                    handler.postDelayed(this, 3000);
                }
            }
        };
        handler.post(paymentCheckRunnable);
    }

    private void checkPaymentStatus(String orderCode, String amount) {
        Map<String, String> body = new HashMap<>();
        body.put("orderCode", orderCode);
        body.put("amount", amount);
        body.put("userId", userId);

        PaymentService service = ApiClient.getClient().create(PaymentService.class);
        service.checkPaymentStatus(body).enqueue(new Callback<PaymentCheckResponse>() {
            @Override
            public void onResponse(@NonNull Call<PaymentCheckResponse> call, @NonNull Response<PaymentCheckResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    isPaymentSuccess = true;
                    handler.removeCallbacks(paymentCheckRunnable);
                    Toast.makeText(CheckoutActivity.this, "✅ Đã thanh toán thành công!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<PaymentCheckResponse> call, @NonNull Throwable t) {
                Toast.makeText(CheckoutActivity.this, "Lỗi kiểm tra thanh toán", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void codCheckout() {
        String userId = getSharedPreferences("MyAppPrefs", MODE_PRIVATE).getString("uId", null);
        if (userId == null) {
            Log.w("CHK_LOG", "How? No user detected");
            RoutingUtils.redirect(this, LoginActivity.class, RoutingUtils.NO_EXTRAS, RoutingUtils.ACTIVITY_FINISH);
            return;
        }
        String note = "There's no note";

        Map<String, String> payload = new HashMap<>();
        payload.put("userId", userId);
        payload.put("note", note);
        payload.put("scheduledTime", null);

        Call<Void> call = orderService.createOrder(payload);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CheckoutActivity.this, "Bon Appetit!", Toast.LENGTH_LONG).show();
                    RoutingUtils.redirect(CheckoutActivity.this, HomeActivity.class, RoutingUtils.NO_EXTRAS, RoutingUtils.ACTIVITY_FINISH);
                } else {
                    Toast.makeText(CheckoutActivity.this, "Không thể tạo đơn", Toast.LENGTH_LONG).show();
                    RoutingUtils.redirect(CheckoutActivity.this, HomeActivity.class, RoutingUtils.NO_EXTRAS, RoutingUtils.ACTIVITY_FINISH);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.e("CHK_LOG", "Order failed: ", t);
                Toast.makeText(CheckoutActivity.this, "Lỗi tạo đơn hàng", Toast.LENGTH_LONG).show();
                RoutingUtils.redirect(CheckoutActivity.this, HomeActivity.class, RoutingUtils.NO_EXTRAS, RoutingUtils.ACTIVITY_FINISH);
            }
        });
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}