package com.example.client.screens.noti.present;

import com.example.client.api.ApiClient;
import com.example.client.api.service.NotificationService;
import com.example.client.app.Preferences;
import com.example.client.models.noti.Notification;
import com.example.client.models.profile.ProfileModel;
import com.example.client.screens.noti.activity.INotificationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationPresent implements INotificationPresent{
    private INotificationView nView;
    private ProfileModel user;
    public NotificationPresent(INotificationView nView){
        this.nView = nView;
        this.user = Preferences.newInstance().getProfile();
    }
    @Override
    public void onShowNotifications() {
        NotificationService service = ApiClient.newInstance().create(NotificationService.class);
        service.getByUser(user.getId()).enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                nView.showNotifications(response.body());
            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {

            }
        });
    }
}
