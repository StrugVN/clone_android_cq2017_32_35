package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

public class SendTextNotificationRequest {
    @SerializedName("tourId")
    int tourId;
    @SerializedName("noti")
    String noti;

    public int getTourId() {
        return tourId;
    }

    public void setTourId(int tourId) {
        this.tourId = tourId;
    }

    public String getNoti() {
        return noti;
    }

    public void setNoti(String noti) {
        this.noti = noti;
    }
}
