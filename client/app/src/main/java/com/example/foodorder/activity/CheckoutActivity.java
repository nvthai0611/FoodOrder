package com.example.foodorder.activity;

import static android.widget.Toast.LENGTH_SHORT;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodorder.Adapter.CartAdapter;
import com.example.foodorder.R;
import com.example.foodorder.models.Cart;
import com.example.foodorder.models.CartItem;
import com.example.foodorder.models.PaymentInfo;
import com.example.foodorder.network.ApiClient;
import com.example.foodorder.network.PaymentService;
import com.example.foodorder.response.PaymentCheckResponse;
import com.example.foodorder.utils.RoutingUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutActivity extends AppCompatActivity {

    private Cart cart;
    private String userId;
    private double totalPrice;
    private boolean isPaymentSuccess = false;
    private Handler handler = new Handler();
    private Runnable paymentCheckRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_checkout);

        userId = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                .getString("uId", null);
        String fullName = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                .getString("uName", null);

        if (userId == null) {
            RoutingUtils.redirect(this, LoginActivity.class, true);
            return;
        }

        cart = (Cart) getIntent().getSerializableExtra("cart");
        if (cart == null) {
            RoutingUtils.redirect(this, HomeActivity.class, true);
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
            public void onResponse(Call<PaymentInfo> call, Response<PaymentInfo> response) {
                if (response.isSuccessful() && response.body() != null) {
                    showPaymentDialog(response.body());
                } else {
                    Toast.makeText(CheckoutActivity.this, "Không lấy được thông tin thanh toán", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PaymentInfo> call, Throwable t) {
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
            public void onResponse(Call<PaymentCheckResponse> call, Response<PaymentCheckResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    isPaymentSuccess = true;
                    handler.removeCallbacks(paymentCheckRunnable);
                    Toast.makeText(CheckoutActivity.this, "✅ Đã thanh toán thành công!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<PaymentCheckResponse> call, Throwable t) {
                Toast.makeText(CheckoutActivity.this, "Lỗi kiểm tra thanh toán", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void codCheckout() {
        Toast.makeText(this, "🚛 Thanh toán khi nhận hàng chưa hỗ trợ", LENGTH_SHORT).show();
    }
}