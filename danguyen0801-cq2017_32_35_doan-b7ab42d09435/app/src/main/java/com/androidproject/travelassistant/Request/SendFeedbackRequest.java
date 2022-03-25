package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

public class SendFeedbackRequest {
    @SerializedName("serviceId")
    private int serviceId;
    @SerializedName("feedback")
    private String feedback;
    @SerializedName("point")
    private int point;

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
