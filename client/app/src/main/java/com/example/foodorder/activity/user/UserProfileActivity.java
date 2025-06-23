package com.example.foodorder.activity.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.foodorder.R;
import com.example.foodorder.activity.FoodActivity;
import com.example.foodorder.activity.LoginActivity;
import com.example.foodorder.models.User;
import com.example.foodorder.network.ApiClient;
import com.example.foodorder.network.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity {
    private EditText text_fullName;
    private EditText text_email;
    private EditText text_phoneNumber;
    private EditText passwordEditText;

    private Button btn_changePass, btn_change;

    private UserService userService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);
        // Lấy token từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        text_fullName = findViewById(R.id.text_fullName);
        text_email = findViewById(R.id.text_email);
        text_phoneNumber = findViewById(R.id.text_phoneNumber);
        passwordEditText = findViewById(R.id.passwordEditText);

        btn_changePass = findViewById(R.id.btn_changePass);
        btn_change = findViewById(R.id.btn_profile);

        userService = ApiClient.getClient().create(UserService.class);
        getUserProfile("1");
    }

    public void getUserProfile (String userId) {
        // goi retrofit
    Call<User> call = userService.getUserById(userId);

    call.enqueue(new Callback<User>() {
        @Override
        public void onResponse(Call<User> call, Response<User> response) {
            System.out.println(response);
            // co data tra ve thi nem vao edittext
            if (response.isSuccessful() && response.body() != null) {
                User user = response.body();
                text_fullName.setText(user.getName());
                text_email.setText(user.getEmail());
                text_phoneNumber.setText(user.getPhone());
                passwordEditText.setText(user.getPassword());

                Toast.makeText(UserProfileActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(UserProfileActivity.this, "Đăng nhập thất bại: Sai username hoặc password", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<User> call, Throwable t) {

        }
    });
    }

}