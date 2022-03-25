package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

public class AddMemberRequest {
    @SerializedName("tourId")
    private int tourId;
    @SerializedName("invitedUserId")
    private int inviteUserId;
    @SerializedName("isInvited")
    private Boolean isInvited;

    public int getTourId() {
        return tourId;
    }

    public void setTourId(int tourId) {
        this.tourId = tourId;
    }

    public int getInviteUserId() {
        return inviteUserId;
    }

    public void setInviteUserId(int inviteUserId) {
        this.inviteUserId = inviteUserId;
    }

    public Boolean getInvited() {
        return isInvited;
    }

    public void setInvited(Boolean invited) {
        isInvited = invited;
    }
}
