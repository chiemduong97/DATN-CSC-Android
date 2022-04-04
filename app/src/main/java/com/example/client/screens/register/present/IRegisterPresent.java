package com.example.client.screens.register.present;

import com.example.client.models.message.MessageModel;

public interface IRegisterPresent {
    void onRegister(String fullname, String email, String password);
    void onGetUserActive(String email, MessageModel mesasge);
    void onUpdateDeviceToken(String email, String token);
}
