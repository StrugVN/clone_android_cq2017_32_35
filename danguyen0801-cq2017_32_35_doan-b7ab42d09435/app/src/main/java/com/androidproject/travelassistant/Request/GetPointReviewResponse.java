package com.androidproject.travelassistant.Request;

import com.androidproject.travelassistant.AppData.ReviewPoint;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetPointReviewResponse {
    @SerializedName("pointStats")
    ArrayList<ReviewPoint> pointStat;

    public ArrayList<ReviewPoint> getPointStat() {
        return pointStat;
    }

    public void setPointStat(ArrayList<ReviewPoint> pointStat) {
        this.pointStat = pointStat;
    }
}
