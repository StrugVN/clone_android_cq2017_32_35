package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

public class ThirdPartyLoginResponse {
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("fullName")
    private String fullName;
    @SerializedName("token")
    private String token;
    @SerializedName("userId")
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
