package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

public class GetCoordinateUsersRequest {
    @SerializedName("userId")
    private int userId;
    @SerializedName("tourId")
    private int tourId;
    @SerializedName("lat")
    private double lat;
    @SerializedName("long")
    private double longg;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTourId() {
        return tourId;
    }

    public void setTourId(int tourId) {
        this.tourId = tourId;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLongg() {
        return longg;
    }

    public void setLongg(double longg) {
        this.longg = longg;
    }
}
