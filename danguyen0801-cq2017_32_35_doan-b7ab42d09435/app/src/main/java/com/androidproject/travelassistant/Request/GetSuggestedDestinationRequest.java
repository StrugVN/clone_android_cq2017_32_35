package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetSuggestedDestinationRequest {
    @SerializedName("hasOneCoordinate")
    private boolean hasOneCoordinate;
    @SerializedName("coordList")
    private ArrayList<CoordSet> coordList;

    public boolean isHasOneCoordinate() {
        return hasOneCoordinate;
    }

    public void setHasOneCoordinate(boolean hasOneCoordinate) {
        this.hasOneCoordinate = hasOneCoordinate;
    }

    public void addToCoordList(double lat1, double lng1, double lat2, double lng2) {
        if (this.coordList == null)
            this.coordList = new ArrayList<>();
        CoordSet new_coordSet = new CoordSet();
        new_coordSet.addToCoordSet(lat1, lng1, lat2, lng2);
        this.coordList.add(new_coordSet);
    }

    private class CoordSet {
        @SerializedName("coordinateSet")
        private ArrayList<SingleCoord> coordSet;

        public ArrayList<SingleCoord> getCoordSet() {
            return coordSet;
        }

        public void setCoordSet(ArrayList<SingleCoord> coordSet) {
            this.coordSet = coordSet;
        }

        public void addToCoordSet(double lat1, double lng1, double lat2, double lng2) {
            if(this.coordSet == null)
                this.coordSet = new ArrayList<>();
            coordSet.add(new SingleCoord(lat1, lng1));
            coordSet.add(new SingleCoord(lat2, lng2));
        }
    }

    private class SingleCoord {
        @SerializedName("lat")
        private double lat;
        @SerializedName("long")
        private double lng;

        public SingleCoord(double lat, double lng) {
            this.lat = lat;
            this.lng = lng;
        }

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
