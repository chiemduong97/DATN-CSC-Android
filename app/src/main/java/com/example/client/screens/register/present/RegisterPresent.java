package com.example.client.screens.register.present;

import android.annotation.SuppressLint;

import com.example.client.api.ApiClient;
import com.example.client.api.service.UserService;
import com.example.client.app.MyFirebaseService;
import com.example.client.app.Preferences;
import com.example.client.models.message.MessageModel;
import com.example.client.models.profile.ProfileModel;
import com.example.client.screens.register.activity.IRegisterView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterPresent implements IRegisterPresent{
    private IRegisterView rView;

    public RegisterPresent(IRegisterView rView) {
        this.rView = rView;
    }
    @SuppressLint("CheckResult")
    @Override
    public void onRegister(String fullname,String email, String password) {
        rView.showLoading();
        MyFirebaseService myFirebaseService =new MyFirebaseService();
        myFirebaseService.getToken().subscribe(o -> {
            UserService service = ApiClient.getInstance().create(UserService.class);
            service.register(fullname,email,password).enqueue(new Callback<MessageModel>() {
                @Override
                public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                    onGetUserActive(email, response.body());
                    String deviceToken = Preferences.getInstance().getDeviceToken();
                    onUpdateDeviceToken(email,deviceToken);

                }

                @Override
                public void onFailure(Call<MessageModel> call, Throwable t) {
                    rView.register(new MessageModel(false,1001,null));
                    rView.hideLoading();
                }
            });
        });

    }

    @Override
    public void onGetUserActive(String email, MessageModel message) {
        Preferences.getInstance().setAccessToken(message.getAccessToken());
        UserService service = ApiClient.getInstance().create(UserService.class);
        service.getUserByEmail(email).enqueue(new Callback<ProfileModel>() {
            @Override
            public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {
                Preferences.getInstance().setProfile(response.body());
                rView.register(message);
                rView.hideLoading();
            }

            @Override
            public void onFailure(Call<ProfileModel> call, Throwable t) {

            }
        });
    }

    @Override
    public void onUpdateDeviceToken(String email, String token) {
        UserService service = ApiClient.getInstance().create(UserService.class);
        service.updateDeviceToken(email,token).enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {

            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {

            }
        });
    }
}
