package com.androidproject.travelassistant.AppData;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Object implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("arrivalAt")
    private long arrivalAt;
    @SerializedName("leaveAt")
    private long leaveAt;
    @SerializedName("maxCost")
    private int maxCost;
    @SerializedName("minCost")
    private int minCost;
    @SerializedName("lat")
    private double lat;
    @SerializedName("long")
    private double longg;
    @SerializedName("name")
    private String name;
    @SerializedName("address")
    private String address;
    @SerializedName("serviceId")
    private int serviceId;
    @SerializedName("serviceTypeId")
    private int serviceTypeId;
    @SerializedName("avatar")
    private String avatar;

    public Object(int id, long arrivalAt, long leaveAt, int maxCost, int minCost, double lat, double longg, String name, String address, int serviceId, int serviceTypeId, String avatar) {
        this.id = id;
        this.arrivalAt = arrivalAt;
        this.leaveAt = leaveAt;
        this.maxCost = maxCost;
        this.minCost = minCost;
        this.lat = lat;
        this.longg = longg;
        this.name = name;
        this.address = address;
        this.serviceId = serviceId;
        this.serviceTypeId = serviceTypeId;
        this.avatar = avatar;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getArrivalAt() {
        return arrivalAt;
    }

    public void setArrivalAt(long arrivalAt) {
        this.arrivalAt = arrivalAt;
    }

    public long getLeaveAt() {
        return leaveAt;
    }

    public void setLeaveAt(long leaveAt) {
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

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
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
