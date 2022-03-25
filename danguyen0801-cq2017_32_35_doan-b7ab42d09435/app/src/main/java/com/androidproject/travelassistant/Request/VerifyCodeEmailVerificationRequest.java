package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

public class VerifyCodeEmailVerificationRequest {
    @SerializedName("userId")
    private int userId;
    @SerializedName("type")
    private String type;
    @SerializedName("verifyCode")
    private String verifyCode;

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

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }
}
