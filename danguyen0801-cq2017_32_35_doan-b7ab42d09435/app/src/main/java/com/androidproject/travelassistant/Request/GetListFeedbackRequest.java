package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

public class GetListFeedbackRequest {
    @SerializedName("serviceId")
    private int serviceId;
    @SerializedName("pageIndex")
    private int pageIndex;
    @SerializedName("pageSize")
    private String pageSize;

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
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
