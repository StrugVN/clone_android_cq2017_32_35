package com.androidproject.travelassistant.AppData;

import com.google.gson.annotations.SerializedName;

public class Feedbacks {
    @SerializedName("userId")
    int userId;
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("feedback")
    private String feedback;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("phone")
    private String phone;
    @SerializedName("point")
    private int point;
    @SerializedName("createdOn")
    private long createdOn;

    public Feedbacks(int id, String name, String feedback, String avatar, String phone, int point, long createdOn) {
        this.id = id;
        this.name = name;
        this.feedback = feedback;
        this.avatar = avatar;
        this.phone = phone;
        this.point = point;
        this.createdOn = createdOn;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public long getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(long createdOn) {
        this.createdOn = createdOn;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
