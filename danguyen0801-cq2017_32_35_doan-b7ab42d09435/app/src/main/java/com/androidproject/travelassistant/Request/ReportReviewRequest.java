package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

public class ReportReviewRequest {
    @SerializedName("reviewId")
    private int reviewId;

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }
}
