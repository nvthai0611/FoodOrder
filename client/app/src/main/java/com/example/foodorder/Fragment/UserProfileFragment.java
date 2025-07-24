package com.example.foodorder.Fragment;

import android.content.Context; // Import Context for SharedPreferences
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.example.foodorder.activity.LoginActivity;
import com.example.foodorder.activity.user.ChangePasswordActivity;
// import com.example.foodorder.activity.user.UserProfileActivity; // Not needed if this is the fragment
import com.example.foodorder.models.User;
import com.example.foodorder.network.ApiClient;
import com.example.foodorder.network.UserService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileFragment extends Fragment {
    private EditText text_fullName, text_email, text_phoneNumber, passwordEditText;
    private Button btn_changePass, btn_change, btn_logOut, btn_testSocket;
    private ImageButton btn_back;
    private UserService userService;
    private String userId;
    private io.socket.client.Socket mSocket;

    {
        try {
            mSocket = IO.socket("http://192.168.0.102:9999");
        } catch (URISyntaxException e) {
            Log.d("myTag", e.getMessage());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Ensure you are inflating the correct layout for the fragment
        // If activity_userprofile.xml is meant for the fragment, this is fine.
        // Otherwise, it should be a layout specifically for the fragment.
        return inflater.inflate(R.layout.activity_userprofile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Access SharedPreferences using requireActivity() or requireContext()
        SharedPreferences prefs = requireActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        userId = prefs.getString("uId", null);

        text_fullName = view.findViewById(R.id.text_fullName);
        text_email = view.findViewById(R.id.text_email);
        text_phoneNumber = view.findViewById(R.id.text_phoneNumber);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        btn_changePass = view.findViewById(R.id.btn_changePass);
        // If btn_back is commented out in XML, remove this too or uncomment it if it exists
        // btn_back = view.findViewById(R.id.backButton);
        btn_change = view.findViewById(R.id.btn_changeProfile);

        userService = ApiClient.getClient().create(UserService.class);

        // If btn_back is commented out in XML, remove this too or uncomment it if it exists
        // btn_back.setOnClickListener(v -> requireActivity().onBackPressed());

        btn_logOut = view.findViewById(R.id.btn_logOut);
        if (btn_logOut == null) {
            Log.e("LogoutDebug", "btn_logOut is null! Check your layout XML ID.");
        } else {
            btn_logOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logout();
                }
            });
        }

        btn_testSocket = view.findViewById(R.id.btn_testSocket);

        mSocket.connect();

        btn_testSocket.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    JSONObject jsonData = new JSONObject();
                    jsonData.put("userId", "666abc000001000000000001");
                    jsonData.put("totalPrice", 2000);
                    jsonData.put("status", "SUCCESS");
                    jsonData.put("timestamp", 1753323882282L);

                    JSONArray itemsArray = new JSONArray();
                    JSONObject item = new JSONObject();
                    item.put("name", "Grilled Chicken Salad");
                    item.put("price", 2000);
                    item.put("quantity", 1);

                    itemsArray.put(item);

                    jsonData.put("items", itemsArray);

                    mSocket.emit("messageFromClient", jsonData);
                } catch (JSONException e) {
                    Log.d("me", "error send message " + e.getMessage());
                }
            }
        });

        btn_changePass.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ChangePasswordActivity.class);
            startActivity(intent);
        });

        btn_change.setOnClickListener(v -> updateUserProfile());

        getUserProfile();
    }

    private void getUserProfile() {
        if (userId == null) {
            Toast.makeText(requireContext(), "User ID not found, please log in again.", Toast.LENGTH_LONG).show();
            // Optionally, redirect to login if userId is null
            startActivity(new Intent(requireContext(), LoginActivity.class));
            requireActivity().finish();
            return;
        }

        Call<User> call = userService.getUserById(userId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    text_fullName.setText(user.getName());
                    text_email.setText(user.getEmail());
                    text_phoneNumber.setText(user.getPhone());
                    // Be careful exposing password directly, even if masked.
                    // Usually, this field would just be for setting a *new* password, not displaying the old one.
                    passwordEditText.setText(user.getPassword());
                } else {
                    Toast.makeText(requireContext(), "Lỗi khi lấy user detail: " + response.code(), Toast.LENGTH_SHORT).show();
                    Log.e("UserProfileFragment", "Error fetching user details: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Toast.makeText(requireContext(), "Lỗi mạng khi lấy thông tin người dùng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("UserProfileFragment", "Network error fetching user details", t);
            }
        });
    }

    private void updateUserProfile() {
        String name = text_fullName.getText().toString().trim();
        String email = text_email.getText().toString().trim();
        String phone = text_phoneNumber.getText().toString().trim();
        // The password field typically isn't updated directly from a profile view unless it's a "Change Password" flow
        // For simplicity, we'll keep it as is based on your original code.
        String password = passwordEditText.getText().toString();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Vui lòng điền đầy đủ tất cả các trường!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userId == null) {
            Toast.makeText(requireContext(), "Không tìm thấy ID người dùng. Vui lòng đăng nhập lại.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(requireContext(), LoginActivity.class));
            requireActivity().finish();
            return;
        }

        User updatedUser = new User(userId, name, email, phone, password); // Ensure your User constructor matches
        Call<User> call = userService.updateUser(userId, updatedUser);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Cập nhật hồ sơ thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Cập nhật thất bại! Mã lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                    Log.e("UserProfileFragment", "Update failed: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Toast.makeText(requireContext(), "Lỗi mạng khi cập nhật hồ sơ: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("UserProfileFragment", "Network error updating profile", t);
            }
        });
    }

    private void logout() {
        // Use requireActivity() or requireContext() to get the Context for SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Xóa toàn bộ thông tin đã lưu
        editor.apply(); // Sử dụng apply() cho thao tác bất đồng bộ, commit() nếu cần đồng bộ ngay lập tức

        Toast.makeText(requireContext(), "Đăng xuất thành công", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(requireContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear backstack
        startActivity(intent);
        // A Fragment cannot call finish() on its own activity.
        // You need to call finish() on the hosting activity.
        requireActivity().finish();
    }
}