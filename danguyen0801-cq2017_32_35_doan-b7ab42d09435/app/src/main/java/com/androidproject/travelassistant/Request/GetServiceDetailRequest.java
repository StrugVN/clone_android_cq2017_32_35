package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

public class GetServiceDetailRequest {
    @SerializedName("serviceId")
    private int setviceId;

    public int getSetviceId() {
        return setviceId;
    }

    public void setSetviceId(int setviceId) {
        this.setviceId = setviceId;
    }
}
