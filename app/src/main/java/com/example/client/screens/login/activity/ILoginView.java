package com.example.client.screens.login.activity;

import com.example.client.models.message.MessageModel;
import com.example.client.models.profile.ProfileModel;

public interface ILoginView {
    void next(MessageModel message);
    void login(MessageModel message);
    void showLoading();
    void hideLoading();
}
