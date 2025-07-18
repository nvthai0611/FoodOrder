package com.example.foodorder.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.foodorder.R;
import com.example.foodorder.activity.user.ChangePasswordActivity;
import com.example.foodorder.models.User;
import com.example.foodorder.network.ApiClient;
import com.example.foodorder.network.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileFragment extends Fragment {
    private EditText text_fullName, text_email, text_phoneNumber, passwordEditText;
    private Button btn_changePass, btn_change;
    private ImageButton btn_back;
    private UserService userService;
    private String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_userprofile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences prefs = requireActivity().getSharedPreferences("MyAppPrefs", getContext().MODE_PRIVATE);
        userId = prefs.getString("uId", null);

        text_fullName = view.findViewById(R.id.text_fullName);
        text_email = view.findViewById(R.id.text_email);
        text_phoneNumber = view.findViewById(R.id.text_phoneNumber);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        btn_changePass = view.findViewById(R.id.btn_changePass);
//        btn_back = view.findViewById(R.id.backButton);
        btn_change = view.findViewById(R.id.btn_changeProfile);

        userService = ApiClient.getClient().create(UserService.class);

//        btn_back.setOnClickListener(v -> requireActivity().onBackPressed());

        btn_changePass.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ChangePasswordActivity.class);
            startActivity(intent);
        });

        btn_change.setOnClickListener(v -> updateUserProfile());

        getUserProfile();
    }

    private void getUserProfile() {
        Call<User> call = userService.getUserById(userId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    text_fullName.setText(user.getName());
                    text_email.setText(user.getEmail());
                    text_phoneNumber.setText(user.getPhone());
                    passwordEditText.setText(user.getPassword());
                } else {
                    Toast.makeText(requireContext(), "Lỗi khi lấy user detail", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Toast.makeText(requireContext(), "Network error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserProfile() {
        String name = text_fullName.getText().toString();
        String email = text_email.getText().toString();
        String phone = text_phoneNumber.getText().toString();
        String password = passwordEditText.getText().toString();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        User updatedUser = new User(userId, name, email, phone, password);
        Call<User> call = userService.updateUser(userId, updatedUser);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Update failed!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Toast.makeText(requireContext(), "Network error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
