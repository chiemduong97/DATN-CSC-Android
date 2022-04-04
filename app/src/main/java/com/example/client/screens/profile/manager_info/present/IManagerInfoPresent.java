package com.example.client.screens.profile.manager_info.present;

import android.graphics.Bitmap;

import com.example.client.models.profile.ProfileModel;

public interface IManagerInfoPresent {
    void onShowInfoUser();
    void onUpdateInfo(ProfileModel user);
    void onUpdatePass(String email,String oldpassword,String newpassword);
    void onUpdateAvatar(String email, Bitmap avatar);
}
