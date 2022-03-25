package com.androidproject.travelassistant.AppData;

import com.google.gson.annotations.SerializedName;

public class UserCoord {
    @SerializedName("id")
    int id;
    @SerializedName("lat")
    double lat;
    @SerializedName("long")
    double lng;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
