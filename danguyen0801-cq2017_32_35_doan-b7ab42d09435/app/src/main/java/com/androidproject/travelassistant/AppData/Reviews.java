package com.androidproject.travelassistant.AppData;

import com.google.gson.annotations.SerializedName;

public class Reviews {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("review")
    private String review;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("point")
    private int point;
    @SerializedName("createdOn")
    private long createOn;

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

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public long getCreateOn() {
        return createOn;
    }

    public void setCreateOn(long createOn) {
        this.createOn = createOn;
    }
}
