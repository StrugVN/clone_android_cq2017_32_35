package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

public class GetDetailResponse {
    @SerializedName("id")
    private int id;
    @SerializedName("address")
    private String address;
    @SerializedName("contact")
    private String contact;
    @SerializedName("lat")
    private int lat;
    @SerializedName("long")
    private int longg;
    @SerializedName("maxCost")
    private int maxCost;
    @SerializedName("minCost")
    private int minCost;
    @SerializedName("selfStarRatings")
    private int selfStarRatings;
    @SerializedName("name")
    private String name;
    @SerializedName("serviceTypeId")
    private int serviceTypeId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
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

    public int getSelfStarratings() {
        return selfStarRatings;
    }

    public void setSelfStarratings(int selfStarratings) {
        this.selfStarRatings = selfStarratings;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(int serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }
}
