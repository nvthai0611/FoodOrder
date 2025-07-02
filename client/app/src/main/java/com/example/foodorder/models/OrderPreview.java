package com.example.foodorder.models;

import java.util.List;

public class OrderPreview {
    private String orderId;
    private String createdAt;
    private String status;
    private double totalPrice;
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

