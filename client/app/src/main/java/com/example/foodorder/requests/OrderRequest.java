package com.example.foodorder.requests;

import com.example.foodorder.models.OrderItem;

import java.util.List;

public class OrderRequest {
    private String user_id;
    private List<OrderItem> items;
    private double total_price;
    private String status_payment = "pending";

    public OrderRequest(String user_id, List<OrderItem> items, double total_price) {
        this.user_id = user_id;
        this.items = items;
        this.total_price = total_price;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public String getStatus_payment() {
        return status_payment;
    }

    public void setStatus_payment(String status_payment) {
        this.status_payment = status_payment;
    }
}