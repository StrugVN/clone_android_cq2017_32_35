package com.androidproject.travelassistant.Request;

import com.androidproject.travelassistant.AppData.Tour;
import com.androidproject.travelassistant.AppData.User;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SearchUserResponse {
    @SerializedName("total")
    private String total;
    @SerializedName("users")
    private ArrayList<User>  users;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
}
