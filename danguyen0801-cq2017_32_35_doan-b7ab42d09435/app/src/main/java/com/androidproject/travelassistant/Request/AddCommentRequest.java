package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

public class AddCommentRequest {
    @SerializedName("tourId")
    private int tourId;
    @SerializedName("comment")
    private String comment;

    public int getTourId() {
        return tourId;
    }

    public void setTourId(int tourId) {
        this.tourId = tourId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
