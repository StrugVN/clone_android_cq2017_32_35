package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

public class GetServiceDetailResponse {
    @SerializedName("id")
    private int id;
    @SerializedName("address")
    private String address;
    @SerializedName("contact")
    private String contact;
    @SerializedName("lat")
    private  double lat;
    @SerializedName("long")
    private double longg;
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
    @SerializedName("avatar")
    private String avatar;

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

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLongg() {
        return longg;
    }

    public void setLongg(double longg) {
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

    public int getSelfStarRatings() {
        return selfStarRatings;
    }

    public void setSelfStarRatings(int selfStarRatings) {
        this.selfStarRatings = selfStarRatings;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
