package com.example.foodorder.activity.user;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.widget.ImageButton;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.example.foodorder.network.ApiClient;
import com.example.foodorder.network.UserService;
import com.example.foodorder.R;
import com.example.foodorder.models.User;
import com.google.android.material.textfield.TextInputEditText;
import android.app.ProgressDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {
    private ImageButton buttonBack;

    private UserService userService;

    private User user;

    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        buttonBack = findViewById(R.id.btnBack);
        buttonBack.setOnClickListener(v -> finish());
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);

        // Retrieve user information from SharedPreferences
        String userId = prefs.getString("uId", null); // Get the user ID
        //Tao cai thang API client de su dụng
        userService = ApiClient.getClient().create(UserService.class);
        getProfileUser(userId);
    }

    private void getProfileUser (String userId){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Call<User> call = userService.getUserById(userId);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()){
                    user = response.body();
                    // Set up button click after user is loaded
                    findViewById(R.id.btnChangePassword).setOnClickListener(v -> {
                        changePassword(userId, user.getPassword());
                    });
                }else{
                    Toast.makeText(ChangePasswordActivity.this,"Lấy thông tin user thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(ChangePasswordActivity.this,"Lỗi đường truyền mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void changePassword(String userId, String currentPassword) {
        String oldPassword = ((TextInputEditText) findViewById(R.id.etOldPassword)).getText().toString();
        String newPassword = ((TextInputEditText) findViewById(R.id.etNewPassword)).getText().toString();
        String confirmPassword = ((TextInputEditText) findViewById(R.id.etConfirmPassword)).getText().toString();

        if (!oldPassword.equals(currentPassword)) {
            Toast.makeText(this, "Current password is incorrect", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "New passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }
        if (oldPassword.equals(newPassword)) {
            Toast.makeText(this, "New password must be different from current password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Call backend to update password
        User updatedUser = new User(userId, user.getName(), user.getEmail(), user.getPhone(), newPassword); // Fill other fields as needed
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        // Goi call cua retrofit
        Call<User> call = userService.updateUser(userId, updatedUser);
        // Giai ma cai thang retrofit no tra ve khi goi API
        call.enqueue(new Callback<User>() {
            //Thanh cong thi ntn
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(ChangePasswordActivity.this, "Password updated successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "Update failed!", Toast.LENGTH_SHORT).show();
                }
            }
            //Loi thi ntn
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(ChangePasswordActivity.this, "Network error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}