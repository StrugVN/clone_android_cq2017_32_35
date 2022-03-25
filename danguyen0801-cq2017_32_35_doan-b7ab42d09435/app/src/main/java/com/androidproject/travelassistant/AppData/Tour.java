package com.androidproject.travelassistant.AppData;

public class Tour {
    private int id, status = 0;
    private String name, avatar;
    private int adults, childs;
    private long minCost, maxCost;
    private double sourceLat, sourceLong, desLat, desLong;
    private long startDate, endDate;
    private boolean isPrivate;

    public Tour() {}

    //Constructor will all parameters
    public Tour(String name, String avatar, long startDate, long endDate, double sourceLat,
                double sourceLong, double desLat, double desLong, int adults, int childs,
                int minCost, int maxCost, boolean isPrivate) {
        this.name = name;
        this.avatar = avatar;
        this.startDate = startDate;
        this.endDate = endDate;
        this.sourceLat = sourceLat;
        this.sourceLong = sourceLong;
        this.desLat = desLat;
        this.desLong = desLong;
        this.adults = adults;
        this.childs = childs;
        this.minCost = minCost;
        this.maxCost = maxCost;
        this.isPrivate = isPrivate;
    }

    //Constructor with required parameters only
    public Tour(String name, long startDate, long endDate, double sourceLat, double sourceLong, double desLat, double desLong, boolean isPrivate) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.sourceLat = sourceLat;
        this.sourceLong = sourceLong;
        this.desLat = desLat;
        this.desLong = desLong;
        this.isPrivate = isPrivate;
    }

    //Get - set
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

    public long getStartDate() {
        return startDate;
    }

    public long getEndDate() {
        return endDate;
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

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
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

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }
}
