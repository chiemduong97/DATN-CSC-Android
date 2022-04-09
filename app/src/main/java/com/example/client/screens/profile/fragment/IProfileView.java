package com.example.client.screens.profile.fragment;

import com.example.client.models.profile.ProfileModel;

public interface IProfileView {
    void logout(boolean isSuccess);
    void showUserInfo(ProfileModel profile);
}
