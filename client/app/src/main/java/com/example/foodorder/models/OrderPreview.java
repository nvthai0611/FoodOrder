package com.example.foodorder.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class OrderPreview {
    @SerializedName("_id")
    private String orderId;

    @SerializedName("created_at")
    private String createdAt;

    private String status;

    @SerializedName("total_price")
    private double totalPrice;

    @SerializedName("food_images")
    private List<String> foodImages;

    public OrderPreview(String orderId, String createdAt, String status, double totalPrice, List<String> foodImages) {
        this.orderId = orderId;
        this.createdAt = createdAt;
        this.status = status;
        this.totalPrice = totalPrice;
        this.foodImages = foodImages;
    }

    public String getOrderId() { return orderId; }
    public String getCreatedAt() { return createdAt; }
    public String getStatus() { return status; }
    public double getTotalPrice() { return totalPrice; }
    public List<String> getFoodImages() { return foodImages; }
}

