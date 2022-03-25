package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

import java.io.File;

public class UpdateUserAvatarRequest {
    @SerializedName("file")
    private String file;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
