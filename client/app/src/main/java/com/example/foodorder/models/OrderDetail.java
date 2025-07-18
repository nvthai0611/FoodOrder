package com.example.foodorder.models;

import java.util.List;

public class OrderDetail {
    public String _id;
    public String user_id;
    public int total_price;
    public String status;
    public String note;
    public String scheduled_time;
    public String created_at;
    public List<String> food_images;
    public List<OrderItem> items;

    public OrderDetail() {
    }

    public OrderDetail(String _id, String user_id, String status, int total_price, String note, String scheduled_time, String created_at, List<OrderItem> items, List<String> food_images) {
        this._id = _id;
        this.user_id = user_id;
        this.status = status;
        this.total_price = total_price;
        this.note = note;
        this.scheduled_time = scheduled_time;
        this.created_at = created_at;
        this.items = items;
        this.food_images = food_images;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getScheduled_time() {
        return scheduled_time;
    }

    public void setScheduled_time(String scheduled_time) {
        this.scheduled_time = scheduled_time;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<String> getFood_images() {
        return food_images;
    }

    public void setFood_images(List<String> food_images) {
        this.food_images = food_images;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
}

