package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

public class VerifyOTPPasswordRecoveryRequest {
    @SerializedName("userId")
    private int userId;
    @SerializedName("newPassword")
    private String newPassword;
    @SerializedName("verifyCode")
    private String verifyCode;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }
}
