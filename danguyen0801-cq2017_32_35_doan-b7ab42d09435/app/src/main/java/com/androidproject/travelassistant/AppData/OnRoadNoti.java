package com.androidproject.travelassistant.AppData;

import com.google.gson.annotations.SerializedName;

public class OnRoadNoti {
    @SerializedName("lat")
    private double lat;
    @SerializedName("long")
    private double lng;
    @SerializedName("note")
    private String note;
    @SerializedName("notificationType")
    private int notificationType;
    @SerializedName("speed")
    private int speed;
    @SerializedName("createdByTourId")
    int tourId;

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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(int notificationType) {
        this.notificationType = notificationType;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getTourId() {
        return tourId;
    }

    public void setTourId(int tourId) {
        this.tourId = tourId;
    }
}
