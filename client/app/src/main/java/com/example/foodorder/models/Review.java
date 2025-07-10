package com.example.foodorder.models;

public class Review {
    private String _id;
    private String user_id;
    private String food_id;
    private int rating;      // từ 1 đến 5
    private String comment;
    private String created_at;

    public Review() {}

    public Review(String _id, String user_id, String food_id, int rating, String comment, String created_at) {
        this._id = _id;
        this.user_id = user_id;
        this.food_id = food_id;
        this.rating = rating;
        this.comment = comment;
        this.created_at = created_at;
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

    public String getFood_id() {
        return food_id;
    }

    public void setFood_id(String food_id) {
        this.food_id = food_id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
