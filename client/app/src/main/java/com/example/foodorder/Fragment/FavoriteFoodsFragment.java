package com.example.foodorder.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorder.R;
import com.example.foodorder.Adapter.FavoriteFoodAdapter;
import com.example.foodorder.models.Food;

import java.util.ArrayList;
import java.util.List;
import android.widget.Toast;
import com.example.foodorder.utils.FavoriteManager;
public class FavoriteFoodsFragment extends Fragment {

    private RecyclerView recyclerFavoriteFoods;
    private TextView tvEmptyState;
    private FavoriteFoodAdapter adapter;
    private List<Food> favoriteFoods;

    public FavoriteFoodsFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_foods, container, false);

        recyclerFavoriteFoods = view.findViewById(R.id.recyclerFavoriteFoods);
        tvEmptyState = view.findViewById(R.id.tvEmptyState);
        // Dùng dữ liệu thật từ SharedPreferences
        FavoriteManager favoriteManager = new FavoriteManager(requireContext());
        favoriteFoods = favoriteManager.getFavorites();

        // ✅ Toast từng món ra để debug
        if (favoriteFoods != null && !favoriteFoods.isEmpty()) {
            for (Food food : favoriteFoods) {
                Toast.makeText(requireContext(),
                        "ID: " + food.getId() + "\nPrice: " + food.getPrice(),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(requireContext(), "Danh sách yêu thích trống", Toast.LENGTH_SHORT).show();
        }

//        favoriteFoods = getDummyFavoriteFoods(); // Hoặc load từ API, Room, SharedPref...
        adapter = new FavoriteFoodAdapter(favoriteFoods, getContext());

        recyclerFavoriteFoods.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerFavoriteFoods.setAdapter(adapter);

        updateEmptyState();

        return view;
    }

    private void updateEmptyState() {
        if (favoriteFoods.isEmpty()) {
            tvEmptyState.setVisibility(View.VISIBLE);
        } else {
            tvEmptyState.setVisibility(View.GONE);
        }
    }

//    private List<Food> getDummyFavoriteFoods() {
//        // Dummy data nếu bạn chưa có backend
//        List<Food> list = new ArrayList<>();
////         list.add(new Food("Pizza", "Delicious Italian pizza", "https://image.url", 100));
//        return list;
//    }
}