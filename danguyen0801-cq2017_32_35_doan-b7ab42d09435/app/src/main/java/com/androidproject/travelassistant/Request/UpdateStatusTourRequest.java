package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

public class UpdateStatusTourRequest {
    @SerializedName("tourId")
    private int tourId;

    public int getTourId() {
        return tourId;
    }

    public void setTourId(int tourId) {
        this.tourId = tourId;
    }
}
