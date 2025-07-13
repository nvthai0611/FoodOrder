package com.example.foodorder.requests;

import com.google.gson.annotations.SerializedName;

public class ReviewRequest {
    @SerializedName("user_id")
    private String userId;

    @SerializedName("food_id")
    private String foodId;

    @SerializedName("rating")
    private int rating;

    @SerializedName("comment")
    private String comment;

    public ReviewRequest(String userId, String foodId, int rating, String comment) {
        this.userId = userId;
        this.foodId = foodId;
        this.rating = rating;
        this.comment = comment;
    }

    public ReviewRequest() {

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
