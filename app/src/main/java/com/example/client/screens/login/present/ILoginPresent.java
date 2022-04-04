package com.example.client.screens.login.present;


import com.example.client.models.message.MessageModel;

public interface ILoginPresent {
    void onNext(String email);
    void onLogin(String email,String password);
    void onSetUserActive(String email, MessageModel message);
    void onUpdateDeviceToken(String email, String token);
}
