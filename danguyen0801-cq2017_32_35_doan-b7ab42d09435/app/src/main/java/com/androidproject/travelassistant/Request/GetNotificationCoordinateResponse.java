package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

public class GetNotificationCoordinateResponse {
    @SerializedName("lat")
    private int lat;
    @SerializedName("longg")
    private int longg;
    @SerializedName("note")
    private String note;
    @SerializedName("notificationType")
    private int notificationType;

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
}
