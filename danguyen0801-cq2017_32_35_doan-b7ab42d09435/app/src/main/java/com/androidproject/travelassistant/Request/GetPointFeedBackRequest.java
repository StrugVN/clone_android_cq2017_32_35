package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

public class GetPointFeedBackRequest {
    @SerializedName("serviceId")
    private int serviceId;

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }
}
