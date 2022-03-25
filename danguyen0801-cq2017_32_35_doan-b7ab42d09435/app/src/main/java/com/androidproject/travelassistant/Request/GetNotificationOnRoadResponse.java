package com.androidproject.travelassistant.Request;

import com.androidproject.travelassistant.AppData.OnRoadNoti;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetNotificationOnRoadResponse {
    @SerializedName("notiList")
    ArrayList<OnRoadNoti> notiList;

    public ArrayList<OnRoadNoti> getNotiList() {
        return notiList;
    }

    public void setNotiList(ArrayList<OnRoadNoti> notiList) {
        this.notiList = notiList;
    }
}