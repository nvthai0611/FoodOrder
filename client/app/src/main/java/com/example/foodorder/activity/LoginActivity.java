package com.example.foodorder.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodorder.R;
import com.example.foodorder.models.User;
import com.example.foodorder.network.ApiClient;
import com.example.foodorder.network.LoginService;
import com.example.foodorder.requests.LoginRequest;
import com.example.foodorder.response.LoginResponse;

import com.example.foodorder.utils.RoutingUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class LoginActivity extends AppCompatActivity {
    private EditText edtUsername, edtPassword;
    private Button btnLogin;
    private LoginService loginService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        edtUsername = findViewById(R.id.username);
        edtPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.loginButton);
        TextView tvSignup = findViewById(R.id.signupText);
        TextView tvForgotPassword = findViewById(R.id.forgotPasswordText);

        loginService = ApiClient.getClient().create(LoginService.class);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edtUsername.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Vui lòng nhập username và password", Toast.LENGTH_SHORT).show();
                    return;
                }

                loginUser(username, password);
            }
        });

        tvSignup.setOnClickListener(v -> {
            RoutingUtils.redirect(LoginActivity.this, RegisterActivity.class, RoutingUtils.NO_EXTRAS, RoutingUtils.ACTIVITY_FINISH);
        });

        tvForgotPassword.setOnClickListener(v -> {
            RoutingUtils.redirect(LoginActivity.this, ForgotPasswordActivity.class, RoutingUtils.NO_EXTRAS, RoutingUtils.ACTIVITY_FINISH);
        });
    }
    private void loginUser(String email, String password) {
        LoginRequest loginRequest = new LoginRequest(email, password);
        Call<LoginResponse> call = loginService.loginUser(loginRequest); // sửa kiểu trả về

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();

                    if (loginResponse.isSuccess()) {
                        User user = loginResponse.getUser();
                        Log.d("LoginActivity", "Đăng nhập thành công: " + user.getName());

                        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("uId", String.valueOf(user.getId()));
                        editor.putString("uName", user.getName());
                        editor.putString("uEmail", user.getEmail());
                        editor.putString("phone",  user.getPhone());
                        editor.apply();

                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        // Đọc message lỗi từ server trả về (ví dụ: sai mật khẩu)
                        String errorMsg = response.errorBody().string(); // cần try/catch
                        Log.e("LoginError", errorMsg);
                        Toast.makeText(LoginActivity.this, "Lỗi: " + errorMsg, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}