package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

public class OTPPasswordRecoveryRequest {
    @SerializedName("type")
    private String type;
    @SerializedName("value")
    private String value;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
