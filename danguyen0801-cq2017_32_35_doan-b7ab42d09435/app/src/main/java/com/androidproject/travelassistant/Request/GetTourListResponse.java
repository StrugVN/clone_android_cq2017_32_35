package com.androidproject.travelassistant.Request;

import android.util.Log;

import com.androidproject.travelassistant.AppData.Tour;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetTourListResponse {
    @SerializedName("total")
    private int total;
    @SerializedName("tours")
    private ArrayList<Tour> tours;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ArrayList<Tour> getTourList(){
        return tours;
    }
}
