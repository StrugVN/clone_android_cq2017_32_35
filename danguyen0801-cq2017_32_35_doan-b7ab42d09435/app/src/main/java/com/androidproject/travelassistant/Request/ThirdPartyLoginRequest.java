package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

public class ThirdPartyLoginRequest {
    @SerializedName("accessToken")
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
