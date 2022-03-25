package com.androidproject.travelassistant.AppData;

import com.google.gson.annotations.SerializedName;

public class Comment {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("comment")
    private String comment;
    @SerializedName("avatar")
    private String avatar;

    public Comment(int id, String name, String comment, String avatar) {
        this.id = id;
        this.name = name;
        this.comment = comment;
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
