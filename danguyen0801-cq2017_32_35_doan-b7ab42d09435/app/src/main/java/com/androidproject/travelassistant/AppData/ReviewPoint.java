package com.androidproject.travelassistant.AppData;

import com.google.gson.annotations.SerializedName;

public class ReviewPoint {
    @SerializedName("point")
    private int point;
    @SerializedName("total")
    private int total;

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
