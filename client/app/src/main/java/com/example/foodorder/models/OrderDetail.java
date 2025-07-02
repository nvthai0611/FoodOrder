package com.example.foodorder.models;

import java.util.List;

public class OrderDetail {
    private String userName;
    private String address;
    private String status;
    private List<OrderItem> items;

    public String getUserName() {
        return userName;
    }

    public String getAddress() {
        return address;
    }

    public String getStatus() {
        return status;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public OrderDetail() {
    }

    public OrderDetail(String userName, String address, String status, List<OrderItem> items) {
        this.userName = userName;
        this.address = address;
        this.status = status;
        this.items = items;
    }
}

