package com.androidproject.travelassistant.Request;

import com.androidproject.travelassistant.AppData.Tours;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import retrofit2.http.Query;

public class SearchHistoryUserResponse {
    @SerializedName("total")
    private int total;
    @SerializedName("tours")
    private ArrayList<Tours> tours;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ArrayList<Tours> getTours() {
        return tours;
    }

    public void setTours(ArrayList<Tours> tours) {
        this.tours = tours;
    }
}
