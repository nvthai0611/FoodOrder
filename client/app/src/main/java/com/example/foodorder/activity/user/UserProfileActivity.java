package com.example.foodorder.activity.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodorder.R;
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

    private Button btn_changePass, btn_change ;

    private ImageButton btn_back;
    private UserService userService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);

        // Retrieve user information from SharedPreferences
        String userId = prefs.getString("uId", null); // Get the user ID
        // Lấy token từ SharedPreferences
        text_fullName = findViewById(R.id.text_fullName);
        text_email = findViewById(R.id.text_email);
        text_phoneNumber = findViewById(R.id.text_phoneNumber);
        passwordEditText = findViewById(R.id.passwordEditText);

        btn_changePass = findViewById(R.id.btn_changePass);
        btn_back = findViewById(R.id.backButton);
        btn_change = findViewById(R.id.btn_changeProfile);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserProfile(userId);
            }
        });

        userService = ApiClient.getClient().create(UserService.class);
        getUserProfile(userId);
    }

    public void getUserProfile (String userId) {
        // goi retrofit
    Call<User> call = userService.getUserById(userId);

    call.enqueue(new Callback<User>() {
        @Override
        public void onResponse(Call<User> call, Response<User> response) {
            // co data tra ve thi nem vao edittext
            if (response.isSuccessful() && response.body() != null) {
                User user = response.body();
                text_fullName.setText(user.getName());
                text_email.setText(user.getEmail());
                text_phoneNumber.setText(user.getPhone());
                passwordEditText.setText(user.getPassword());

//                Toast.makeText(UserProfileActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(UserProfileActivity.this, "Lỗi khi lấy user detail", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<User> call, Throwable t) {

        }
    });
    }

    private void updateUserProfile(String userId) {
        String name = text_fullName.getText().toString();
        String email = text_email.getText().toString();
        String phone = text_phoneNumber.getText().toString();
        String password = passwordEditText.getText().toString();
        if(name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        User updatedUser = new User(userId, name, email, phone, password);

        Call<User> call = userService.updateUser(userId, updatedUser);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(UserProfileActivity.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UserProfileActivity.this, "Update failed!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(UserProfileActivity.this, "Network error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}