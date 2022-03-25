package com.androidproject.travelassistant.Request;

import com.androidproject.travelassistant.AppData.Comment;
import com.androidproject.travelassistant.AppData.Members;
import com.androidproject.travelassistant.AppData.StopPoints;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetTourInfoResponse {
    @SerializedName("id")
    private int id;
    @SerializedName("hostId")
    private String hostId;
    @SerializedName("status")
    private int status;
    @SerializedName("name")
    private String name;
    @SerializedName("minCost")
    private long minCost;
    @SerializedName("maxCost")
    private long maxCost;
    @SerializedName("startDate")
    private long startDate;
    @SerializedName("endDate")
    private long endDate;
    @SerializedName("adults")
    private int adults;
    @SerializedName("childs")
    private int childs;
    @SerializedName("isPrivate")
    private boolean isPrivate;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("stopPoints")
    private ArrayList<StopPoints> stopPoints;
    @SerializedName("comments")
    private ArrayList<Comment> comment;
    @SerializedName("members")
    private ArrayList<Members> members;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public ArrayList<StopPoints> getStopPoints() {
        return stopPoints;
    }

    public void setStopPoints(ArrayList<StopPoints> stopPoints) {
        this.stopPoints = stopPoints;
    }

    public ArrayList<Comment> getComment() {
        return comment;
    }

    public void setComment(ArrayList<Comment> comment) {
        this.comment = comment;
    }

    public ArrayList<Members> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<Members> members) {
        this.members = members;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
