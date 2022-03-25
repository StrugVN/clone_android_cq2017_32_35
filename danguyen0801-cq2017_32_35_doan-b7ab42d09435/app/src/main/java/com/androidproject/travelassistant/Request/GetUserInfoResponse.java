package com.androidproject.travelassistant.Request;

import com.google.gson.annotations.SerializedName;

public class GetUserInfoResponse {
        @SerializedName("id")
        private int id;
        @SerializedName("fullName")
        private String  full_name;
        @SerializedName("email")
        private String email;
        @SerializedName("phone")
        private String phone;
        @SerializedName("address")
        private String address;
        @SerializedName("dob")
        private String dob;
        @SerializedName("gender")
        private int gender;
        @SerializedName("email_verified")
        private boolean email_verified;
        @SerializedName("phone_verified")
        private boolean phone_verified;
        @SerializedName("avatar")
        private String avatar;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getFull_name() {
            return full_name;
        }

        public void setFull_name(String full_name) {
            this.full_name = full_name;
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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public boolean isEmail_verified() {
            return email_verified;
        }

        public void setEmail_verified(boolean email_verified) {
            this.email_verified = email_verified;
        }

        public boolean isPhone_verified() {
            return phone_verified;
        }

        public void setPhone_verified(boolean phone_verified) {
            this.phone_verified = phone_verified;
        }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}


