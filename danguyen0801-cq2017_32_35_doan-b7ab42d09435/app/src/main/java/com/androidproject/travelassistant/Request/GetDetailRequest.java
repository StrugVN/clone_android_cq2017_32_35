package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

public class GetDetailRequest {
    @SerializedName("serviceId")
    private int serviceId;

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }
}
