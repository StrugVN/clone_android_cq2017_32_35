package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

public class UpdatePasswordRequest {
    @SerializedName("userId")
    private int userId;
    @SerializedName("currentPassword")
    private String currentPassword;
    @SerializedName("newPassword")
    private String newPassword;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
