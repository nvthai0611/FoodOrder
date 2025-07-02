package com.example.foodorder.models;

import java.util.List;

public class OrderDetail {
    private Order order;
    private List<OrderItem> items;

    public OrderDetail() {
    }

    public OrderDetail(Order order, List<OrderItem> items) {
        this.order = order;
        this.items = items;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
}

