package com.example.foodorder.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.foodorder.models.Food;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FavoriteManager {
    private static final String PREF_NAME = "favorites";
    private static final String KEY_FAVORITES = "favorite_food_list";

    private SharedPreferences prefs;
    private Gson gson;

    public FavoriteManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    // Thêm món ăn yêu thích
    public void addFavorite(Food food) {
        List<Food> favorites = getFavorites();
        for (Food f : favorites) {
            if (f.getId().equals(food.getId())) return; // tránh thêm trùng
        }
        favorites.add(food);
        saveFavorites(favorites);
    }

    // Xoá món ăn khỏi yêu thích
    public void removeFavorite(String foodId) {
        List<Food> favorites = getFavorites();
        favorites.removeIf(f -> f.getId().equals(foodId));
        saveFavorites(favorites);
    }

    // Kiểm tra món đã yêu thích chưa
    public boolean isFavorite(String foodId) {
        List<Food> favorites = getFavorites();
        for (Food f : favorites) {
            if (f.getId().equals(foodId)) return true;
        }
        return false;
    }

    // Lấy danh sách món yêu thích
    public List<Food> getFavorites() {
        String json = prefs.getString(KEY_FAVORITES, null);
        if (json == null) return new ArrayList<>();
        Type type = new TypeToken<List<Food>>() {}.getType();
        return gson.fromJson(json, type);
    }

    // Xoá toàn bộ yêu thích (nếu cần)
    public void clearFavorites() {
        prefs.edit().remove(KEY_FAVORITES).apply();
    }

    // Lưu danh sách vào SharedPreferences
    private void saveFavorites(List<Food> favorites) {
        String json = gson.toJson(favorites);
        prefs.edit().putString(KEY_FAVORITES, json).apply();
    }
}
