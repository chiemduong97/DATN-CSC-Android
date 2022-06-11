package com.example.client.screens.register.present;

public interface IRegisterPresent {
    void onRegister(String fullname,String phone, String email, String password);
    void onGetUserActive(String email);
    void onUpdateDeviceToken(String email, String token);
    void sendEmail(String email);
    void verification(String email, String code, String fullname, String phone, String password);
}
