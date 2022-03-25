package com.androidproject.travelassistant.API;

import android.text.TextUtils;

import com.androidproject.travelassistant.AppData.Global;
import com.androidproject.travelassistant.AppData.MyApplication;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class API_Client {
    private static API_Client instance = null;

    private static Retrofit adapter = null;

    private API_Client() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Response response = chain.proceed(request);
                        return response;
                    }
                })
                .build();



        adapter = new Retrofit.Builder()
                .baseUrl(Global.API_Endpoint)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Retrofit getAdapter() {
        return adapter;
    }

    public static API_Client getInstance() {
        if (instance == null)
            instance = new API_Client();
        return instance;
    }
}
