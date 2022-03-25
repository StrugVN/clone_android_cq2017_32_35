package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

public class GetCommentListRequest {
    @SerializedName("tourId")
    private String tourId;
    @SerializedName("pageIndex")
    private int pageIndex;
    @SerializedName("pageSize")
    private String pageSize;

    public String getTourId() {
        return tourId;
    }

    public void setTourId(String tourId) {
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
