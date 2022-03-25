package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

public class AddMemberResponse {
    @SerializedName("message")
    private String message;
    @SerializedName("resCode")
    private int resCode;

    public String getMessage() {
        return message;
    }

    public int getResCode() {
        return resCode;
    }

    public void setResCode(int resCode) {
        this.resCode = resCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

