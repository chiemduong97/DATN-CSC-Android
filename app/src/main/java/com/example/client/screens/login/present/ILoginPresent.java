package com.example.client.screens.login.present;


public interface ILoginPresent {
    void checkEmail(String email);
    void onLogin(String email,String password);
    void setUserActive(String email);
    void onUpdateDeviceToken(String email, String token);
}
