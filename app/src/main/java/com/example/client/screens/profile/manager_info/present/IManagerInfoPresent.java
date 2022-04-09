package com.example.client.screens.profile.manager_info.present;

import android.graphics.Bitmap;

import com.example.client.models.profile.ProfileModel;

public interface IManagerInfoPresent {
    void getUserFromRes();
    void updateProfile(ProfileModel user);
    void updatePassword(String email, String oldpassword, String newpassword);
    void updateAvatar(String email, Bitmap avatar);
}
