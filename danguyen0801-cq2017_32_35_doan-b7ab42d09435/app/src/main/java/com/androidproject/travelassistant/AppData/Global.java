package com.androidproject.travelassistant.AppData;

import com.androidproject.travelassistant.API.UserService;

import java.util.regex.Pattern;

public class Global {
    public static final String API_Endpoint ="http://35.197.153.192:3000";
    public static final int MAX_TOUR_PER_PAGE = 10;
    public static String userToken;
    public static int userId;
    public static int lastLoginType;
    public static UserService userService;

    public static String lastId;
    public static String lastPass;

    public static final int LAST_LOGIN_BY_FB = 1;
    public static final int LAST_LOGIN_BY_GG = 2;
    public static final int LAST_LOGIN_BY_EMAIL = 0;
    public static final int LAST_LOGIN_NONE = -1;

    public static boolean isEmailValid(String email) {
        Pattern pattern = Pattern.compile("^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
        return pattern.matcher(email).matches();
    }

    public static boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    public static boolean isNumberString(String str){
        Pattern pattern = Pattern.compile("^[0-9]*$");
        return pattern.matcher(str).matches();
    }
}
