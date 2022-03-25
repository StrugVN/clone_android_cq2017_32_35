package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

public class UpdateLandingTimesRequest {
    @SerializedName("desId")
    private int desId;

    public int getDesId() {
        return desId;
    }

    public void setDesId(int desId) {
        this.desId = desId;
    }
}
