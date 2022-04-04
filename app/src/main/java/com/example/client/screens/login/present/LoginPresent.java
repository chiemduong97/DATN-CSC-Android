package com.example.client.screens.login.present;

import android.annotation.SuppressLint;

import com.example.client.api.ApiClient;
import com.example.client.api.service.UserService;
import com.example.client.app.MyFirebaseService;
import com.example.client.app.Preferences;
import com.example.client.models.message.MessageModel;
import com.example.client.models.profile.ProfileModel;
import com.example.client.screens.login.activity.ILoginView;

import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPresent implements ILoginPresent{
    private ILoginView lView;

    public LoginPresent(ILoginView lView){
        this.lView = lView;
    }
    @Override
    public void onNext(String email) {
        lView.showLoading();
        UserService service = ApiClient.getInstance().create(UserService.class);
        service.checkEmail(email).enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                lView.next(response.body());
                lView.hideLoading();
            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
                lView.next(new MessageModel(false,1001,null));
                lView.hideLoading();
            }
        });
    }

    @SuppressLint("CheckResult")
    @Override
    public void onLogin(String email,String password) {
        lView.showLoading();
        MyFirebaseService myFirebaseService = new MyFirebaseService();
        myFirebaseService.getToken().subscribe(o -> {
            UserService service = ApiClient.getInstance().create(UserService.class);
            service.login(email,password).enqueue(new Callback<MessageModel>() {
                @Override
                public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                    onSetUserActive(email, response.body());
                    String deviceToken = Preferences.getInstance().getDeviceToken();
                    onUpdateDeviceToken(email,deviceToken);
                }

                @Override
                public void onFailure(Call<MessageModel> call, Throwable t) {
                    lView.login(new MessageModel(false,1001,null));
                    lView.hideLoading();
                }
            });
        });
    }
    @Override
    public void onSetUserActive(String email, MessageModel message){
        Preferences.getInstance().setAccessToken(message.getAccessToken());
        UserService service = ApiClient.getInstance().create(UserService.class);
        service.getUserByEmail(email).enqueue(new Callback<ProfileModel>() {
            @Override
            public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {
                Preferences.getInstance().setProfile(response.body());
                lView.login(message);
                lView.hideLoading();
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
