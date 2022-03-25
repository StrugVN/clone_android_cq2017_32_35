package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

public class RemoveStopPointRequest {
    @SerializedName("stopPointId")
    private String stopPointId;

    public String getStopPointId() {
        return stopPointId;
    }

    public void setStopPointId(String stopPointId) {
        this.stopPointId = stopPointId;
    }
}
