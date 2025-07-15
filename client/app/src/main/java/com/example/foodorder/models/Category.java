package com.example.foodorder.models;

import java.io.Serializable;

public class Category implements Serializable {
    private String _id;
    private String name;
    private boolean is_available;
    private String created_at;

    public String getId() { return _id; }
    public void setId(String _id) { this._id = _id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isAvailable() { return is_available; }
    public void setAvailable(boolean is_available) { this.is_available = is_available; }

    public String getCreatedAt() { return created_at; }
    public void setCreatedAt(String created_at) { this.created_at = created_at; }
}
