package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

public class CreateTourRequest {
    @SerializedName("name")
    private String name;
    @SerializedName("startDate")
    private long startDate;
    @SerializedName("endDate")
    private long endDate;
    @SerializedName("sourceLat")
    private double sourceLat;
    @SerializedName("sourceLong")
    private double sourceLong;
    @SerializedName("desLat")
    private double desLat;
    @SerializedName("desLong")
    private double desLong;
    @SerializedName("isPrivate")
    private Boolean isPrivate;
    @SerializedName("adults")
    private int adults;
    @SerializedName("childs")
    private int childs;
    @SerializedName("minCost")
    private int minCost;
    @SerializedName("maxCost")
    private int maxCost;
    @SerializedName("avatar")
    private String avatar;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setSourceLat(int sourceLat) {
        this.sourceLat = sourceLat;
    }

    public void setSourceLong(int sourceLong) {
        this.sourceLong = sourceLong;
    }

    public void setDesLat(int desLat) {
        this.desLat = desLat;
    }

    public void setDesLong(int desLong) {
        this.desLong = desLong;
    }

    public Boolean getPrivate() {
        return isPrivate;
    }

    public void setPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public double getSourceLat() {
        return sourceLat;
    }

    public void setSourceLat(double sourceLat) {
        this.sourceLat = sourceLat;
    }

    public double getSourceLong() {
        return sourceLong;
    }

    public void setSourceLong(double sourceLong) {
        this.sourceLong = sourceLong;
    }

    public double getDesLat() {
        return desLat;
    }

    public void setDesLat(double desLat) {
        this.desLat = desLat;
    }

    public double getDesLong() {
        return desLong;
    }

    public void setDesLong(double desLong) {
        this.desLong = desLong;
    }
}
