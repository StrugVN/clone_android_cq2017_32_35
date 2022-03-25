package com.androidproject.travelassistant.Request;

import com.androidproject.travelassistant.AppData.StopPoints;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetHistoryDestinationResponse {
   @SerializedName("total")
    private int total;
   @SerializedName("stopPoints")
    private ArrayList<StopPoints> stopPoints;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ArrayList<StopPoints> getStopPoints() {
        return stopPoints;
    }

    public void setStopPoints(ArrayList<StopPoints> stopPoints) {
        this.stopPoints = stopPoints;
    }
}
