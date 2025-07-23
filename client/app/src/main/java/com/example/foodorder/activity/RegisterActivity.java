package com.example.foodorder.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodorder.R;
import com.example.foodorder.models.User;
import com.example.foodorder.network.ApiClient;
import com.example.foodorder.network.RegisterService;
import com.example.foodorder.requests.RegisterRequest;
import com.example.foodorder.utils.AuthUtils;
import com.example.foodorder.utils.RoutingUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private RegisterService registerService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        Button btnRegister = findViewById(R.id.registerButton);
        Button btnRegisterWithFacebook = findViewById(R.id.btnContinueWithFacebook);
        Button btnRegisterWithGoogle = findViewById(R.id.btnContinueWithGoogle);
        TextView tvToLogin = findViewById(R.id.signinText);

        registerService = ApiClient.getClient().create(RegisterService.class);

        btnRegister.setOnClickListener(v -> {
            createAccount();
        });

        btnRegisterWithFacebook.setOnClickListener(v -> {
            createAccountWithFacebook();
        });

        btnRegisterWithGoogle.setOnClickListener(v -> {
            createAccountWithGoogle();
        });

        tvToLogin.setOnClickListener(v -> {
            RoutingUtils.redirect(this, LoginActivity.class, RoutingUtils.NO_EXTRAS, RoutingUtils.ACTIVITY_FINISH);
        });
    }

    //Unable to test since connection to server is unavailable
    private void createAccount() {
        EditText edtUsername = findViewById(R.id.username);
        EditText edtUserEmail = findViewById(R.id.userEmail);
        EditText edtPassword = findViewById(R.id.password);
        EditText edtConfirmPassword = findViewById(R.id.confirmPassword);

        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String email = edtUserEmail.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();

        if (username.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            Toast.makeText(RegisterActivity.this, "Vui lòng nhập đầy đủ nội dung", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!AuthUtils.isEmailValid(email)){
            Toast.makeText(RegisterActivity.this, "Email sai cú pháp, vui lòng nhập lại", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(RegisterActivity.this, "Mật khẩu không trùng khớp, vui lòng nhập lại", Toast.LENGTH_SHORT).show();
            return;
        }

        RegisterRequest request = new RegisterRequest(username, password, email);
        Call<User> call = registerService.registerUser(request);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Đăng kí thành công!", Toast.LENGTH_SHORT).show();
                    RoutingUtils.redirect(RegisterActivity.this, LoginActivity.class, RoutingUtils.NO_EXTRAS, RoutingUtils.ACTIVITY_FINISH);
                } else {
                    Toast.makeText(RegisterActivity.this, "Đăng ký thất bại! Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Toast.makeText(RegisterActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createAccountWithFacebook() {
        //TODO: Implement logic
    }

    private void createAccountWithGoogle() {
        //TODO: Implement logic
    }
}
