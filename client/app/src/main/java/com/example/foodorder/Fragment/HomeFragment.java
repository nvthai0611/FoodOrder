package com.example.foodorder.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.foodorder.Adapter.BannerAdapter;
import com.example.foodorder.Adapter.CategoryAdapter;
import com.example.foodorder.Adapter.FoodAdapter;
import com.example.foodorder.R;
import com.example.foodorder.activity.CartActivity;
import com.example.foodorder.models.Category;
import com.example.foodorder.models.Food;
import com.example.foodorder.network.ApiClient;
import com.example.foodorder.network.HomeService;
import com.example.foodorder.utils.RoutingUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 1));

        fetchFoodsBestSellerList();
        adapterBestSeller = new FoodAdapter(requireContext(), foodsBestSellerList, true);
        recyclerView.setAdapter(adapterBestSeller);

        // cho phần Foos
        fetchCategories();
        rvCategories = view.findViewById(R.id.rvCategories);
        rvFoods = view.findViewById(R.id.rvFoods);
        fetchFoods();

        List<Integer> images = new ArrayList<Integer>();
        images.add(R.drawable.banner_home);
        images.add(R.drawable.banner2);
        images.add(R.drawable.banner3);
        BannerAdapter bannerAdapter = new BannerAdapter(images);
        viewPager.setAdapter(bannerAdapter);
        TabLayout tabLayout = view.findViewById(R.id.bannerIndicator);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setIcon(R.drawable.tab_selector); // bạn cần tạo drawable này
        }).attach();


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton btnCart = requireActivity().findViewById(R.id.btnCart);
        btnCart.setOnClickListener(v -> {
            RoutingUtils.redirect(HomeFragment.this.getActivity(), CartActivity.class, RoutingUtils.NO_EXTRAS, RoutingUtils.ACTIVITY_KEEP);
        });
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
                if (isAdded() && getActivity() != null) {
                    Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "API call failed", t);
                }
            }
        });
    }

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

//                    Log.e(TAG, "Response success:");
//                    for (Food food : allFoods) {
//                        Log.e(TAG, "Food item: " +
//                                "\n - Name: " + food.getName() +
//                                "\n - Description: " + food.getDescription() +
//                                "\n - Price: " + food.getPrice() +
//                                "\n - Image URL: " + food.getImageUrl() +
//                                "\n - Category: " + (food.getCategory() != null ? food.getCategory().getName() : "null"));
//                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to get data", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Response error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Food>> call, Throwable t) {
                if (isAdded() && getActivity() != null) {
                    Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "API call failed", t);
                }
            }
        });
    }

    private void fetchFoodsByCategoryId(String id) {
        HomeService homeService = ApiClient.getClient().create(HomeService.class);
        Call<List<Food>> call = homeService.getFoodsByCategory(id);

        call.enqueue(new Callback<List<Food>>() {
            @Override
            public void onResponse(Call<List<Food>> call, Response<List<Food>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    filteredFoods.clear();
                    filteredFoods.addAll(response.body());
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
                if (isAdded() && getActivity() != null) {
                    Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "API call failed", t);
                }
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
                    var c = new Category();
                    c.setId("");
                    c.setName("All");
                    categoryList.add(c);
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
                if (isAdded() && getActivity() != null) {
                    Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "API call failed", t);
                }
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
        adapter = new FoodAdapter(requireContext(), filteredFoods, false);
        rvFoods.setAdapter(adapter);
    }

    private void filterFoodByCategory(String categoryId) {
        if (categoryId.isBlank()) {
            fetchFoods();
            return;
        }
        fetchFoodsByCategoryId(categoryId);
    }
}
