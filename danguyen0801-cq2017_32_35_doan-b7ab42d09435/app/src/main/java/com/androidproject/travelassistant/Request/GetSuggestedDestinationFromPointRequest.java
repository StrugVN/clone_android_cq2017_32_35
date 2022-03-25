package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetSuggestedDestinationFromPointRequest {
    @SerializedName("hasOneCoordinate")
    private boolean hasOneCoordinate;
    @SerializedName("coordList")
    private SingleCoord coordList;

    public boolean isHasOneCoordinate() {
        return hasOneCoordinate;
    }

    public void setHasOneCoordinate(boolean hasOneCoordinate) {
        this.hasOneCoordinate = hasOneCoordinate;
    }

    public SingleCoord getCoordList() {
        return coordList;
    }

    public void setCoordList(SingleCoord coordList) {
        this.coordList = coordList;
    }

    public void setCoordList(double lat, double lng) {
        if (this.coordList == null) this.coordList = new SingleCoord();
        this.coordList.setLat(lat);
        this.coordList.setLng(lng);
    }

    private class SingleCoord{
        @SerializedName("lat")
        private double lat;
        @SerializedName("long")
        private double lng;

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }
    }
}

