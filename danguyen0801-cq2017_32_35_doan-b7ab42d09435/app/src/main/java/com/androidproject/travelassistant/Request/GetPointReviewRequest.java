package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

import retrofit2.http.Query;

public class GetPointReviewRequest {
    @SerializedName("tourId")
    private int tourId;

    public int getTourId() {
        return tourId;
    }

    public void setTourId(int tourId) {
        this.tourId = tourId;
    }
}
