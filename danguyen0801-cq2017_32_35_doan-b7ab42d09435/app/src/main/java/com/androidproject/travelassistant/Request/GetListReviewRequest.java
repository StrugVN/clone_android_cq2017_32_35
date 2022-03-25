package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

import retrofit2.http.Query;

public class GetListReviewRequest {
    @SerializedName("tourId")
    private int tourId;
    @SerializedName("pageIndex")
    private int pageIndex;
    @SerializedName("pageSize")
    private String pageSize;

    public int getTourId() {
        return tourId;
    }

    public void setTourId(int tourId) {
        this.tourId = tourId;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }
}
