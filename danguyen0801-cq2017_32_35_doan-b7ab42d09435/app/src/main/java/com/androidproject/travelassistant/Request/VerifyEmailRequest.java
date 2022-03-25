package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

public class VerifyEmailRequest {
    @SerializedName("userId")
    private int userId;
    @SerializedName("type")
    private String type;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
