package com.androidproject.travelassistant.AppData;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    private int id;
    @SerializedName("fullName")
    private String fullName;
    @SerializedName("email")
    private String email;
    @SerializedName("phone")
    private String phone;
    @SerializedName("gender")
    private int gender;
    @SerializedName("dob")
    private String dob;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("typeLogin")
    private int typeLogin;

    public User(){ }

    public User(int id, String fullName, String email, String phone, int gender, String dob, String avatar) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.dob = dob;
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getTypeLogin() {
        return typeLogin;
    }

    public void setTypeLogin(int typeLogin) {
        this.typeLogin = typeLogin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {

        this.avatar = (avatar == null ? "" : avatar);
    }
}
