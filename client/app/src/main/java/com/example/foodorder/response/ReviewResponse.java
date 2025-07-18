package com.example.foodorder.response;

import com.example.foodorder.requests.ReviewRequest;
import com.google.gson.annotations.SerializedName;

public class ReviewResponse {
    @SerializedName("message")
    private String message;

    @SerializedName("review")
    private ReviewRequest review;


    public ReviewResponse(String message, ReviewRequest review) {
        this.message = message;
        this.review = review;
    }

    public String getMessage() {
        return message;
    }

    public ReviewRequest getReview() {
        return review;
    }
}
