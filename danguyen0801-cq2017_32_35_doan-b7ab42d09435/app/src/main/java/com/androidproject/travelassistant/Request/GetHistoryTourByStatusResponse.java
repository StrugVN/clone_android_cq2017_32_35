package com.androidproject.travelassistant.Request;

import com.androidproject.travelassistant.AppData.TotalTourGroupedByStatus;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetHistoryTourByStatusResponse {
    @SerializedName("totalTourGroupByStatus")
    private ArrayList<TotalTourGroupedByStatus> totalTourGroupByStatus;

    public ArrayList<TotalTourGroupedByStatus> getTotalTourGroupByStatus() {
        return totalTourGroupByStatus;
    }

    public void setTotalTourGroupByStatus(ArrayList<TotalTourGroupedByStatus> totalTourGroupByStatus) {
        this.totalTourGroupByStatus = totalTourGroupByStatus;
    }
}
