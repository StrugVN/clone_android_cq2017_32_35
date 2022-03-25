package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

public class VerifyCodeEmailVerificationResponse {
    @SerializedName("success")
    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
