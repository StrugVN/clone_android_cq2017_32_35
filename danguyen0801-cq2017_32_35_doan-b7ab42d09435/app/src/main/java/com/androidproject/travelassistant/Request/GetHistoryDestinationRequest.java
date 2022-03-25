package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

public class GetHistoryDestinationRequest {
    @SerializedName("tourId")
    private String tourId;
    @SerializedName("name")
    private String name;
    @SerializedName("lat")
    private int lat;
    @SerializedName("long")
    private int longg;

    public String getTourId() {
        return tourId;
    }

    public void setTourId(String tourId) {
        this.tourId = tourId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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
