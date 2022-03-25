package com.androidproject.travelassistant.Request;

import com.androidproject.travelassistant.AppData.StopPoints;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AddStopPointRequest {
    @SerializedName("tourId")
    private int tourId;
    @SerializedName("stopPoints")
    private ArrayList<StopPoints> stopPoints;
    private ArrayList<Integer> deleteIds;

    public int getTourId() {
        return tourId;
    }

    public void setTourId(int tourId) {
        this.tourId = tourId;
    }

    public ArrayList<StopPoints> getStopPoints() {
        return stopPoints;
    }

    public void setStopPoints(ArrayList<StopPoints> stopPoints) {
        this.stopPoints = stopPoints;
    }

    public ArrayList<Integer> getDeleteIds() {
        return deleteIds;
    }

    public void setDeleteIds(ArrayList<Integer> deleteIds) {
        this.deleteIds = deleteIds;
    }
}
