package com.example.client.screens.profile.manager_info;

import com.example.client.models.message.MessageModel;
import com.example.client.models.profile.ProfileModel;

public interface IManagerInfoView {
    void showInfoUser(ProfileModel user);
    void updateInfo(MessageModel message);
    void updatePass(MessageModel message);
    void updateAvatar(MessageModel message);
    void showLoading();
    void hideLoading();
}
