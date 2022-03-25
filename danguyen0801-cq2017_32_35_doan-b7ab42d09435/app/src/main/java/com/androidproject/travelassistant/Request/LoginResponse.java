package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("userId")
    private int userId;
    @SerializedName("emailVerified")
    private boolean emailVerified;
    @SerializedName("phoneVerified")
    private boolean phoneVerified;
    @SerializedName("token")
    private String token;
    @SerializedName("message")
    private String message;

    public int getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public boolean isPhoneVerified() {
        return phoneVerified;
    }

    public void setPhoneVerified(boolean phoneVerified) {
        this.phoneVerified = phoneVerified;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
