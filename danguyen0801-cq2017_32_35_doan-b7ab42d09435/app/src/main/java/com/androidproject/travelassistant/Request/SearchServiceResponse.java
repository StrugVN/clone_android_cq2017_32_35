package com.androidproject.travelassistant.Request;

import com.androidproject.travelassistant.AppData.Service;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SearchServiceResponse {
    @SerializedName("stopPoints")
    ArrayList<Service> stopPoints;

    public ArrayList<Service> getStopPoints() {
        return stopPoints;
    }

    public void setStopPoints(ArrayList<Service> stopPoints) {
        this.stopPoints = stopPoints;
    }
}
