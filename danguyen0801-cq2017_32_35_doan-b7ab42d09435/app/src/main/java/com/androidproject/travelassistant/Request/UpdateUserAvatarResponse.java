package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

public class UpdateUserAvatarResponse {
    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
