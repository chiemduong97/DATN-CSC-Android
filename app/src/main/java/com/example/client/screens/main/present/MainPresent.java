package com.example.client.screens.main.present;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.client.R;
import com.example.client.api.ApiClient;
import com.example.client.api.service.UserService;
import com.example.client.app.Preferences;
import com.example.client.models.profile.ProfileModel;
import com.example.client.screens.home.fragment.HomeFragment;
import com.example.client.screens.main.activity.IMainView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPresent implements IMainPresent {

    private IMainView mView;
    private ProfileModel user;

    public MainPresent(IMainView mView){
        user = Preferences.getInstance().getProfile();
        onSetUserActive();
        this.mView = mView;
    }

    @Override
    public void replaceFragment(int id) {
        switch (id){
            case R.id.menu_home:
                mView.showHomeScreen();
                break;
            case R.id.menu_wallet:
                mView.showNotiScreen();
                break;
            case R.id.menu_profile:
                mView.showProfileScreen();
                break;
        }
    }

    @Override
    public void onSetUserActive() {
        UserService service = ApiClient.getInstance().create(UserService.class);
        service.getUserByEmail(user.getEmail()).enqueue(new Callback<ProfileModel>() {
            @Override
            public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {
                Preferences.getInstance().setProfile(response.body());
            }

            @Override
            public void onFailure(Call<ProfileModel> call, Throwable t) {

            }
        });
    }
}
