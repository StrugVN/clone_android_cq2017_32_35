package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

public class GetHistoryTourRequest {
    @SerializedName("pageIndex")
    private int pageIndex;
    @SerializedName("pageSize")
    private int pageSize;

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
