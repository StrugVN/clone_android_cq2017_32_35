package com.androidproject.travelassistant.Request;

import com.androidproject.travelassistant.AppData.StopPoints;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetSuggestedDestinationResponse {
    @SerializedName("stopPoints")
    private ArrayList<StopPoints> stopPoints;

    public ArrayList<StopPoints> getStopPoints() {
        return stopPoints;
    }

    public void setStopPoints(ArrayList<StopPoints> stopPoints) {
        this.stopPoints = stopPoints;
    }
}
