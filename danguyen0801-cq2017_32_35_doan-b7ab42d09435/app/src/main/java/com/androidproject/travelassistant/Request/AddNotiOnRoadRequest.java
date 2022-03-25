package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

public class AddNotiOnRoadRequest {
    @SerializedName("lat")
    double lat;
    @SerializedName("long")
    double lng;
    @SerializedName("tourId")
    int tourId;
    @SerializedName("userId")
    int userId;
    @SerializedName("notificationType")
    int notiType;
    @SerializedName("note")
    String note;
    @SerializedName("speed")
    int speed;

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

    public int getTourId() {
        return tourId;
    }

    public void setTourId(int tourId) {
        this.tourId = tourId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getNotiType() {
        return notiType;
    }

    public void setNotiType(int notiType) {
        this.notiType = notiType;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
