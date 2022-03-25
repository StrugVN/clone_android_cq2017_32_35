package com.androidproject.travelassistant.AppData;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.lang.reflect.Member;

public class Members implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("phone")
    private String phone;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("isHost")
    private boolean isHost;

    public Members(){}

    public Members(int id, String name, String phone, String avatar, boolean isHost) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.avatar = avatar;
        this.isHost = isHost;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isHost() {
        return isHost;
    }

    public void setHost(boolean host) {
        isHost = host;
    }
}
