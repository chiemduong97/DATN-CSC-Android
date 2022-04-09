package com.example.client.screens.profile.manager_info;

import com.example.client.models.profile.ProfileModel;

public interface IManagerInfoView {
    void showUserInfo(ProfileModel user);
    void updateInfo();
    void updatePass();
    void updateAvatar();
    void showLoading();
    void hideLoading();
    void showErrorMessage(int errMessage);
}
