package com.androidproject.travelassistant.AppData;

import com.google.gson.annotations.SerializedName;

public class Tours_Invitation {
    @SerializedName("id")
    private int id;
    @SerializedName("status")
    private int status;
    @SerializedName("hostId")
    private int hostId;
    @SerializedName("adults")
    private int adults;
    @SerializedName("chils")
    private int childs;
    @SerializedName("isPrivate")
    private boolean isPrivate;
    @SerializedName("minCost")
    private long  minCost;
    @SerializedName("maxCost")
    private long maxCost;
    @SerializedName("startDate")
    private long startDate;
    @SerializedName("endDate")
    private long endDate;
    @SerializedName("hostName")
    private String hostName;
    @SerializedName("hostPhone")
    private String hostPhone;
    @SerializedName("hostEmail")
    private String hostEmail;
    @SerializedName("hostAvatar")
    private String hostAvatar;
    @SerializedName("name")
    private String name;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("createOn")
    private String createdOn;

    public Tours_Invitation(int id, int status, int hostId, int adults, int childs, boolean isPrivate, long minCost, long maxCost, long startDate, long endDate, String hostName, String hostPhone, String hostEmail, String hostAvatar, String name, String avatar, String createdOn) {
        this.id = id;
        this.status = status;
        this.hostId = hostId;
        this.adults = adults;
        this.childs = childs;
        this.isPrivate = isPrivate;
        this.minCost = minCost;
        this.maxCost = maxCost;
        this.startDate = startDate;
        this.endDate = endDate;
        this.hostName = hostName;
        this.hostPhone = hostPhone;
        this.hostEmail = hostEmail;
        this.hostAvatar = hostAvatar;
        this.name = name;
        this.avatar = avatar;
        this.createdOn = createdOn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getHostId() {
        return hostId;
    }

    public void setHostId(int hostId) {
        this.hostId = hostId;
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

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public long getMinCost() {
        return minCost;
    }

    public void setMinCost(long minCost) {
        this.minCost = minCost;
    }

    public long getMaxCost() {
        return maxCost;
    }

    public void setMaxCost(long maxCost) {
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

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getHostPhone() {
        return hostPhone;
    }

    public void setHostPhone(String hostPhone) {
        this.hostPhone = hostPhone;
    }

    public String getHostEmail() {
        return hostEmail;
    }

    public void setHostEmail(String hostEmail) {
        this.hostEmail = hostEmail;
    }

    public String getHostAvatar() {
        return hostAvatar;
    }

    public void setHostAvatar(String hostAvatar) {
        this.hostAvatar = hostAvatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }
}
