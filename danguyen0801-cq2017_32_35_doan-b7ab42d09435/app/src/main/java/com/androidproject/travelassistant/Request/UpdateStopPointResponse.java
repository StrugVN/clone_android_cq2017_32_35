package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

public class UpdateStopPointResponse {
    @SerializedName("tourId")
    private int tourId;
    @SerializedName("name")
    private String name;
    @SerializedName("lat")
    private int lat;
    @SerializedName("longg")
    private int longg;
    @SerializedName("arrivalAt")
    private int arrivalAt;
    @SerializedName("leaveAt")
    private int leaveAt;
    @SerializedName("maxCost")
    private int maxCost;
    @SerializedName("minCost")
    private int minCost;
    @SerializedName("serviceTypeId")
    private int serviceTypeId;
    @SerializedName("id")
    private String id;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("index")
    private int index;

    public int getTourId() {
        return tourId;
    }

    public void setTourId(int tourId) {
        this.tourId = tourId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLat() {
        return lat;
    }

    public void setLat(int lat) {
        this.lat = lat;
    }

    public int getLongg() {
        return longg;
    }

    public void setLongg(int longg) {
        this.longg = longg;
    }

    public int getArrivalAt() {
        return arrivalAt;
    }

    public void setArrivalAt(int arrivalAt) {
        this.arrivalAt = arrivalAt;
    }

    public int getLeaveAt() {
        return leaveAt;
    }

    public void setLeaveAt(int leaveAt) {
        this.leaveAt = leaveAt;
    }

    public int getMaxCost() {
        return maxCost;
    }

    public void setMaxCost(int maxCost) {
        this.maxCost = maxCost;
    }

    public int getMinCost() {
        return minCost;
    }

    public void setMinCost(int minCost) {
        this.minCost = minCost;
    }

    public int getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(int serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}

