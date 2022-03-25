package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

public class VerifyEmailResponse {
    @SerializedName("userId")
    private int userId;
    @SerializedName("sendTo")
    private String sendTo;
    @SerializedName("expiredon")
    private long expiredon;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }

    public long getExpiredon() {
        return expiredon;
    }

    public void setExpiredon(long expiredon) {
        this.expiredon = expiredon;
    }
}
