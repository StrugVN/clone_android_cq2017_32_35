package com.androidproject.travelassistant.Request;

import com.androidproject.travelassistant.AppData.Reviews;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetListReviewResponse {
    @SerializedName("total")
    private int total;
    @SerializedName("reviewList")
    private ArrayList<Reviews> reviews;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ArrayList<Reviews> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Reviews> reviews) {
        this.reviews = reviews;
    }
}
