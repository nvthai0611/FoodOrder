package com.example.foodorder.models;
import java.util.List;

import java.util.List;

public class Order {
    private String _id;
    private String user_id;
    private String status_payment;
    private String status;
    private String note;
    private String scheduled_time;
    private String created_at;
    private int total_price;
    private List<OrderItem> items;

    public Order() {
    }

    public Order(String _id, String user_id, String note, String status, String status_payment, String scheduled_time, String created_at, int total_price, List<OrderItem> items) {
        this._id = _id;
        this.user_id = user_id;
        this.note = note;
        this.status = status;
        this.status_payment = status_payment;
        this.scheduled_time = scheduled_time;
        this.created_at = created_at;
        this.total_price = total_price;
        this.items = items;
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

    public String getStatus_payment() {
        return status_payment;
    }

    public void setStatus_payment(String status_payment) {
        this.status_payment = status_payment;
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

    public String getScheduled_time() {
        return scheduled_time;
    }

    public void setScheduled_time(String scheduled_time) {
        this.scheduled_time = scheduled_time;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
}
