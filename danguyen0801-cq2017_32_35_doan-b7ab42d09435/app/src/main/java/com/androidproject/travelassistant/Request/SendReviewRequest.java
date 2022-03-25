package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

public class SendReviewRequest {
    @SerializedName("tourId")
    private int tourId;
    @SerializedName("point")
    private int point;
    @SerializedName("review")
    private String review;

    public int getTourId() {
        return tourId;
    }

    public void setTourId(int tourId) {
        this.tourId = tourId;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
