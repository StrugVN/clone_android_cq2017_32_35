package com.androidproject.travelassistant.AppData;

import com.google.gson.annotations.SerializedName;

public class TotalTourGroupedByStatus {
    @SerializedName("status")
    private int status;
    @SerializedName("total")
    private int total;

    public TotalTourGroupedByStatus(int status, int total) {
        this.status = status;
        this.total = total;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
