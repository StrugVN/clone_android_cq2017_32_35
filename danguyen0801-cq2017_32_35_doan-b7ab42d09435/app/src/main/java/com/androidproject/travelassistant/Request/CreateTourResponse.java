package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

public class CreateTourResponse {
    @SerializedName("hostId")
    private String hostId;
    @SerializedName("status")
    private int status;
    @SerializedName("name")
    private String name;
    @SerializedName("minCost")
    private int minCost;
    @SerializedName("maxCost")
    private int maxCost;
    @SerializedName("startDate")
    private long startDate;
    @SerializedName("endDate")
    private long endDate;
    @SerializedName("adults")
    private int adults;
    @SerializedName("childs")
    private int childs;
    @SerializedName("sourceLat")
    private int sourceLat;
    @SerializedName("sourceLong")
    private int sourdceLong;
    @SerializedName("desLat")
    private int desLat;
    @SerializedName("desLong")
    private int desLong;
    @SerializedName("id")
    private int id;
    @SerializedName("isPrivate")
    private boolean isPrivate;
    @SerializedName("avatar")
    private String avatar;

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMinCost() {
        return minCost;
    }

    public void setMinCost(int minCost) {
        this.minCost = minCost;
    }

    public int getMaxCost() {
        return maxCost;
    }

    public void setMaxCost(int maxCost) {
        this.maxCost = maxCost;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public int getAdults() {
        return adults;
    }

    public void setAdults(int adults) {
        this.adults = adults;
    }

    public int getChilds() {
        return childs;
    }

    public void setChilds(int childs) {
        this.childs = childs;
    }

    public int getSourceLat() {
        return sourceLat;
    }

    public void setSourceLat(int sourceLat) {
        this.sourceLat = sourceLat;
    }

    public int getSourdceLong() {
        return sourdceLong;
    }

    public void setSourdceLong(int sourdceLong) {
        this.sourdceLong = sourdceLong;
    }

    public int getDesLat() {
        return desLat;
    }

    public void setDesLat(int desLat) {
        this.desLat = desLat;
    }

    public int getDesLong() {
        return desLong;
    }

    public void setDesLong(int desLong) {
        this.desLong = desLong;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
