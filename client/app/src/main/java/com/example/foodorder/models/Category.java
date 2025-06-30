package com.example.foodorder.models;

public class Category {
    private String id;
    private String name;
    private Boolean is_available;
    private String created_at;

    public Category(String id, String name, Boolean is_available, String created_at) {
        this.id = id;
        this.name = name;
        this.is_available = is_available;
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIs_available() {
        return is_available;
    }

    public void setIs_available(Boolean is_available) {
        this.is_available = is_available;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
