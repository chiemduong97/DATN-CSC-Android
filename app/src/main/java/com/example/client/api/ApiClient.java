package com.example.client.api;

import com.example.client.app.Preferences;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit retrofit;
    public static Retrofit getInstance(){
        String token = Preferences.getInstance().getAccessToken();
        String baseUrl = "http://10.82.128.55:8585/";
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(chain -> {
            Request request = chain.request().newBuilder()
                    .addHeader("Authorization","Bearer "+token)
                    .build();
            return chain.proceed(request);
        }).build();
        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

}
