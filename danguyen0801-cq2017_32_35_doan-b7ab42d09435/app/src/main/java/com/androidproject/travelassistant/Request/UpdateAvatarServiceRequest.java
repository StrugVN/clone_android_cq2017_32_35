package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

import java.io.File;

public class UpdateAvatarServiceRequest {
    @SerializedName("file")
    private File file;
    @SerializedName("serviceId")
    private String serviceId;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}
