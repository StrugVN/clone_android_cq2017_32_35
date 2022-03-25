package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

public class OTPPasswordRecoveryResponse {
    @SerializedName("type")
    private String type;
    @SerializedName("expiredon")
    private String expiredon;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExpiredon() {
        return expiredon;
    }

    public void setExpiredon(String expiredon) {
        this.expiredon = expiredon;
    }
}
