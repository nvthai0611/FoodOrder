package com.example.foodorder.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.foodorder.Adapter.BannerAdapter;
import com.example.foodorder.Adapter.CategoryAdapter;
import com.example.foodorder.Adapter.FoodAdapter;
import com.example.foodorder.R;
import com.example.foodorder.models.Category;
import com.example.foodorder.models.Food;
import com.example.foodorder.network.ApiClient;
import com.example.foodorder.network.HomeService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    List<Food> foodsBestSellerList = new ArrayList<>();
    private static final String TAG = "HomeActivity";
    RecyclerView rvCategories;
    FoodAdapter adapterBestSeller;
    FoodAdapter adapter;
    RecyclerView rvFoods;


    private List<Category> categoryList = new ArrayList<>();
    private List<Food> allFoods = new ArrayList<>();
    private List<Food> filteredFoods = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ViewPager2 viewPager = view.findViewById(R.id.bannerViewPager);
        RecyclerView recyclerView = view.findViewById(R.id.rvPopularFood);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));

        fetchFoodsBestSellerList();
        adapterBestSeller = new FoodAdapter(requireContext(), foodsBestSellerList);
        recyclerView.setAdapter(adapterBestSeller);


        // cho phần Foos
        fetchCategories();
        rvCategories = view.findViewById(R.id.rvCategories);
        rvFoods = view.findViewById(R.id.rvFoods);
        fetchFoods();

        List<Integer> images = Arrays.asList(R.drawable.banner_home);
        BannerAdapter bannerAdapter = new BannerAdapter(images);
        viewPager.setAdapter(bannerAdapter);
        return view;
    }
    private void fetchFoodsBestSellerList() {
        HomeService homeService = ApiClient.getClient().create(HomeService.class);
        Call<List<Food>> call = homeService.getAllFoodsBestSeller();

        call.enqueue(new Callback<List<Food>>() {
            @Override
            public void onResponse(Call<List<Food>> call, Response<List<Food>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    foodsBestSellerList.clear();
                    foodsBestSellerList.addAll(response.body());
                    adapterBestSeller.notifyDataSetChanged();

                    Log.e(TAG, "Response success:");
                    for (Food food : foodsBestSellerList) {
                        Log.e(TAG, "Food item: " +
                                "\n - Name: " + food.getName() +
                                "\n - Description: " + food.getDescription() +
                                "\n - Price: " + food.getPrice() +
                                "\n - Image URL: " + food.getImageUrl() +
                                "\n - Category: " + (food.getCategory() != null ? food.getCategory().getName() : "null"));
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to get data", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Response error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Food>> call, Throwable t) {
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "API call failed", t);
            }
        });
    }

    public interface OnFoodsLoadedListener {
        void onLoaded(List<Food> foods);
    }
//    public interface OnCategoriesLoadedListener {
//        void onLoaded(List<Category> categories);
//    }
//    private void fetchFoods(OnFoodsLoadedListener listener) {
//        HomeService homeService = ApiClient.getClient().create(HomeService.class);
//        Call<List<Food>> call = homeService.getAllFoods();
//
//        call.enqueue(new Callback<List<Food>>() {
//            @Override
//            public void onResponse(Call<List<Food>> call, Response<List<Food>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    allFoods.clear();
//                    allFoods.addAll(response.body());
//                    if (listener != null) {
//                        listener.onLoaded(allFoods);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Food>> call, Throwable t) {
//                Toast.makeText(requireContext(), "Load foods failed", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    private void fetchFoods() {
        HomeService homeService = ApiClient.getClient().create(HomeService.class);
        Call<List<Food>> call = homeService.getAllFoods();

        call.enqueue(new Callback<List<Food>>() {
            @Override
            public void onResponse(Call<List<Food>> call, Response<List<Food>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allFoods.clear();
                    allFoods.addAll(response.body());
                    adapter.notifyDataSetChanged();

                    Log.e(TAG, "Response success:");
                    for (Food food : allFoods) {
                        Log.e(TAG, "Food item: " +
                                "\n - Name: " + food.getName() +
                                "\n - Description: " + food.getDescription() +
                                "\n - Price: " + food.getPrice() +
                                "\n - Image URL: " + food.getImageUrl() +
                                "\n - Category: " + (food.getCategory() != null ? food.getCategory().getName() : "null"));
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to get data", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Response error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Food>> call, Throwable t) {
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "API call failed", t);
            }
        });
    }

    private void fetchCategories() {
        HomeService homeService = ApiClient.getClient().create(HomeService.class);
        Call<List<Category>> call = homeService.getAllCategories();

        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoryList.clear();
                    categoryList.addAll(response.body());

                    Log.e(TAG, "Response success:");
                    for (Category category : categoryList) {
                        Log.e(TAG, "Category item: " +
                                "\n - Name: " + category.getName());
                    }
                    setupCategoriesAndFoods();
                } else {
                    Toast.makeText(requireContext(), "Failed to get data", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Response error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "API call failed", t);
            }
        });
    }

    private void setupCategoriesAndFoods() {
        // Gán adapter cho category
        CategoryAdapter categoryAdapter = new CategoryAdapter(categoryList, categoryId -> {
            filterFoodByCategory(categoryId);
        });
        rvCategories.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        rvCategories.setAdapter(categoryAdapter);

        // Gán adapter cho food
        filteredFoods = allFoods;
        adapter = new FoodAdapter(requireContext(), filteredFoods);
        rvFoods.setAdapter(adapter);
    }
    private void filterFoodByCategory(String categoryId) {
        filteredFoods.clear();
        fetchFoods();
        for (Food food : allFoods) {
            if (food.getCategory().getId().equals(categoryId)) {
                filteredFoods.add(food);
            }
        }

        adapter.notifyDataSetChanged();
    }
}
