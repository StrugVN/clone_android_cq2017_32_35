package com.androidproject.travelassistant.Request;

import com.androidproject.travelassistant.AppData.TextNoti;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetNotificationListResponse {
    @SerializedName("notiList")
    ArrayList<TextNoti> notiList;

    public ArrayList<TextNoti> getNotiList() {
        return notiList;
    }

    public void setNotiList(ArrayList<TextNoti> notiList) {
        this.notiList = notiList;
    }
}
