package com.example.foodorder.models;

public class Order {

    private String id;
    private String userId;
    private double totalPrice;
    private String status;         // pending / preparing / done / canceled
    private String note;
    private String scheduledTime;
    private String createdAt;

    public Order() {
    }

    public Order(String userId, String id, double totalPrice, String status, String note, String scheduledTime, String createdAt) {
        this.userId = userId;
        this.id = id;
        this.totalPrice = totalPrice;
        this.status = status;
        this.note = note;
        this.scheduledTime = scheduledTime;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(String scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
