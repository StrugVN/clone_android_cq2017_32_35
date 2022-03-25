package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

public class GetCommentListResponse {
    @SerializedName("userId")
    private int userId;
    @SerializedName("name")
    private String name;
    @SerializedName("comment")
    private String comment;
    @SerializedName("avatar")
    private String avatar;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommment() {
        return comment;
    }

    public void setCommment(String commment) {
        this.comment = commment;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
