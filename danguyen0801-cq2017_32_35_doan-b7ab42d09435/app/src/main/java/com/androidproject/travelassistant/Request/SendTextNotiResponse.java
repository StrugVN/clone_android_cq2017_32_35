package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

public class SendTextNotiResponse {
    @SerializedName("message")
    String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
