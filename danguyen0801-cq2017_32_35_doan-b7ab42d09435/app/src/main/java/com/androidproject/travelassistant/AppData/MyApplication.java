package com.androidproject.travelassistant.AppData;

import android.app.Application;
import android.app.Application;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;
import android.text.TextUtils;

import com.androidproject.travelassistant.API.API_Client;
import com.androidproject.travelassistant.API.UserService;
import com.androidproject.travelassistant.R;
import com.androidproject.travelassistant.Utility.Utility;
import com.google.android.libraries.places.api.Places;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.Locale;

public class MyApplication extends Application {
    public void setAccessToken(String token, int id, int loginType, String emailPhone, String pass){
        Global.userToken = token;
        Global.userId = id;
        Global.lastId = emailPhone;
        Global.lastPass = pass;
        setTokenInfo(token, id, loginType, emailPhone, pass);
    }

    public void setAccessToken(String token, int id, int loginType){
        Global.userToken = token;
        Global.userId = id;
        setTokenInfo(token, id, loginType);
    }

    public void setTokenInfo(String tokenInfo, int id, int loginType, String emailPhone, String pass) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString("saved_token", tokenInfo); // saved_token
        editor.putInt("saved_id", id);

        editor.putInt("last_login", loginType);
        editor.putString("login_id", emailPhone);
        editor.putString("login_pass", pass);

        editor.apply();
    }

    public void setTokenInfo(String tokenInfo, int id, int loginType) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString("saved_token", tokenInfo); // saved_token
        editor.putInt("saved_id", id);

        editor.putInt("last_login", loginType);
        editor.putString("login_id", "");
        editor.putString("login_pass", "");

        editor.apply();
    }

    public void loadTokenInfo() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String tokenStr = sharedPref.getString("saved_token", null);
        int id = sharedPref.getInt("saved_id", -1);

        Global.userToken = tokenStr;
        Global.userId = id;

        Global.lastLoginType = sharedPref.getInt("last_login", -1);
        Global.lastId = sharedPref.getString("login_id", null);
        Global.lastPass = sharedPref.getString("login_pass", null);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        loadTokenInfo();
        Utility.initProvinceIds();
        Global.userService = API_Client.getInstance().getAdapter().create(UserService.class);
    }
}
