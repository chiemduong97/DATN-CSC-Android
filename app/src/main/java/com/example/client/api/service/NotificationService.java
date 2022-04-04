package com.example.client.api.service;

import com.example.client.models.noti.Notification;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NotificationService {
    @GET("views/noti/getByUser.php")
    Call<List<Notification>> getByUser(@Query("user") int user);
}
