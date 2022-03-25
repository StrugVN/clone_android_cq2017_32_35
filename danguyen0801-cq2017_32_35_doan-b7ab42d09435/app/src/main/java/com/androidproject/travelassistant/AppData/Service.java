package com.androidproject.travelassistant.AppData;

import com.google.gson.annotations.SerializedName;

public class Service {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("address")
    private String address;
    @SerializedName("provinceId")
    private int provinceId;
    @SerializedName("contact")
    private String contact;
    @SerializedName("selfStarRating")
    private String selfStarRating;
    @SerializedName("lat")
    private double lat;
    @SerializedName("long")
    private double longg;
    @SerializedName("minCost")
    private int minCost;
    @SerializedName("maxCost")
    private int maxCost;
    @SerializedName("serviceTypeId")
    private int serviceTypeId;
    @SerializedName("serviceId")
    private String avatar;
    @SerializedName("landingTimes")
    private int landingTimes;

    public Service(int id, String name, String address, int provinceId, double lat, double longg, int minCost, int maxCost, int serviceTypeId, String avatar) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.provinceId = provinceId;
        this.contact = contact;
        this.lat = lat;
        this.longg = longg;
        this.minCost = minCost;
        this.maxCost = maxCost;
        this.serviceTypeId = serviceTypeId;
        this.avatar = avatar;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getSelfStarRating() {
        return selfStarRating;
    }

    public void setSelfStarRating(String selfStarRating) {
        this.selfStarRating = selfStarRating;
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

    public int getLandingTimes() {
        return landingTimes;
    }

    public void setLandingTimes(int landingTimes) {
        this.landingTimes = landingTimes;
    }
}
