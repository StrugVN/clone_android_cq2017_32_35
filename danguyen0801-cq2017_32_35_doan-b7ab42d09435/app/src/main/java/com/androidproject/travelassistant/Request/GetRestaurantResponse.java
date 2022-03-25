package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

public class GetRestaurantResponse {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("price")
    private int price;
    @SerializedName("isFamous")
    private boolean isFamous;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isFamous() {
        return isFamous;
    }

    public void setFamous(boolean famous) {
        isFamous = famous;
    }
}
