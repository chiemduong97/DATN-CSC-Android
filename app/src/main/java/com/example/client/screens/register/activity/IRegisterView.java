package com.example.client.screens.register.activity;

public interface IRegisterView {
    void register();
    void showVerificationDialog(boolean showDescription);
    void showLoading();
    void hideLoading();
    void showErrorMessage(int errMessage);
}
