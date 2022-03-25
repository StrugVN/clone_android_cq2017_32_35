package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

public class SearchServiceRequest {
    @SerializedName("searchKey")
    private String searchKey;
    @SerializedName("provinceId")
    private int provinceId;
    @SerializedName("provinceName")
    private String provinceName;
    @SerializedName("pageIndex")
    private int pageIndex;
    @SerializedName("pageSize")
    private int pageSize;

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

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
