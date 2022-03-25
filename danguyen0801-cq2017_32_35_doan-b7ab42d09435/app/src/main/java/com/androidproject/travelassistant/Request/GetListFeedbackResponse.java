package com.androidproject.travelassistant.Request;

import com.androidproject.travelassistant.AppData.Feedbacks;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetListFeedbackResponse {
    @SerializedName("total")
    private int total;
    @SerializedName("feedbackList")
    private ArrayList<Feedbacks> feedbacks;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ArrayList<Feedbacks> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(ArrayList<Feedbacks> feedbacks) {
        this.feedbacks = feedbacks;
    }
}
