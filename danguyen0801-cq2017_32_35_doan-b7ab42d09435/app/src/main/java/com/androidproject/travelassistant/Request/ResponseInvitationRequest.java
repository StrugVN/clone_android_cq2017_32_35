package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

public class ResponseInvitationRequest {
    @SerializedName("tourId")
    private int tourId;
    @SerializedName("isAccepted")
    private Boolean isAccepted;

    public int getTourId() {
        return tourId;
    }

    public void setTourId(int tourId) {
        this.tourId = tourId;
    }

    public Boolean getAccepted() {
        return isAccepted;
    }

    public void setAccepted(Boolean accepted) {
        isAccepted = accepted;
    }
}
