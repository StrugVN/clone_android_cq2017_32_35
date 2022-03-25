package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

import retrofit2.http.Query;

public class ReportFeedbackRequest {
    @SerializedName("feedbackId")
    private int feedbackId;

    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }
}
