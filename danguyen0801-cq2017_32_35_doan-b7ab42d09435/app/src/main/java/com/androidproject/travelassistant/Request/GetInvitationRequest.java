package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

public class GetInvitationRequest {
    @SerializedName("pageIndex")
    private int pageIndex;
    @SerializedName("pageSize")
    private String pageSize;

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
