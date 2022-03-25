package com.androidproject.travelassistant.Request;

import com.androidproject.travelassistant.AppData.Tours_Invitation;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetInvitationUsersResponse {
    @SerializedName("total")
    private int total;
    @SerializedName("tours")
    private ArrayList<Tours_Invitation> tours;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ArrayList<Tours_Invitation> getTours() {
        return tours;
    }

    public void setTours(ArrayList<Tours_Invitation> tours) {
        this.tours = tours;
    }
}
