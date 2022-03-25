package com.androidproject.travelassistant.AppData;

public class Tours {
    private int id, status, adults, childs;
    private long minCost, maxCost, startDate, endDate;
    private String name, avatar;
    private boolean isPrivate, isHost, isKicked;

    public Tours(int id, int status, int adults, int childs, long minCost, long maxCost, long startDate, long endDate, String name, String avatar, boolean isPrivate, boolean isHost, boolean isKicked) {
        this.id = id;
        this.status = status;
        this.adults = adults;
        this.childs = childs;
        this.minCost = minCost;
        this.maxCost = maxCost;
        this.startDate = startDate;
        this.endDate = endDate;
        this.name = name;
        this.avatar = avatar;
        this.isPrivate = isPrivate;
        this.isHost = isHost;
        this.isKicked = isKicked;
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

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public boolean isHost() {
        return isHost;
    }

    public void setHost(boolean host) {
        isHost = host;
    }

    public boolean isKicked() {
        return isKicked;
    }

    public void setKicked(boolean kicked) {
        isKicked = kicked;
    }
}
