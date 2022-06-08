package com.example.client.screens.profile.present;

import com.example.client.api.ApiClient;
import com.example.client.api.service.UserService;
import com.example.client.app.MyFirebaseService;
import com.example.client.app.Preferences;
import com.example.client.models.message.MessageModel;
import com.example.client.models.profile.ProfileModel;
import com.example.client.screens.profile.fragment.IProfileView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfilePresent implements IProfilePresent {
    private IProfileView pView;
    private ProfileModel user;
    public ProfilePresent(IProfileView pView) {
        this.pView = pView;
        user = Preferences.getInstance().getProfile();
    }

    @Override
    public void onLogout() {
        MyFirebaseService myFirebaseService = new MyFirebaseService();
        myFirebaseService.deleteToken();
        UserService service = ApiClient.getInstance().create(UserService.class);
//        service.updateDeviceToken(user.getEmail(),null).enqueue(new Callback<MessageModel>() {
//            @Override
//            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
//                if(response.body().isStatus()){
//                    Preferences.getInstance().deleteDeviceToken();
//                    Preferences.getInstance().deleteProfile();
//                    Preferences.getInstance().deleteAccessToken();
//                    Preferences.getInstance().deleteCart();
//                    pView.logout(response.body().isStatus());
//                }
//                else {
//                    pView.logout(false);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MessageModel> call, Throwable t) {
//                pView.logout(false);
//            }
//        });
    }

    @Override
    public void getUserFromRes() {
        pView.showUserInfo(Preferences.getInstance().getProfile());
    }
}
