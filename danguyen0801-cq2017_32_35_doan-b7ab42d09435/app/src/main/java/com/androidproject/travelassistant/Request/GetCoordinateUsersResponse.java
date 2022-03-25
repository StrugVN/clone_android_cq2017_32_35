package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

public class GetCoordinateUsersResponse {
    @SerializedName("lat")
    private int lat;
    @SerializedName("long")
    private int longg;

    public int getLat() {
        return lat;
    }

    public void setLat(int lat) {
        this.lat = lat;
    }

    public int getLongg() {
        return longg;
    }

    public void setLongg(int longg) {
        this.longg = longg;
    }
}
