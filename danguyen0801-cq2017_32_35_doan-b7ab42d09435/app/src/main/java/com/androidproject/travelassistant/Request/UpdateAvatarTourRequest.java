package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

import java.io.File;

public class UpdateAvatarTourRequest {
    @SerializedName("file")
    private File file;
    @SerializedName("tourId")
    private String tourId;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getTourId() {
        return tourId;
    }

    public void setTourId(String tourId) {
        this.tourId = tourId;
    }
}
