package com.example.client.screens.login.activity;

public interface ILoginView {
    void next();
    void login();
    void showLoading();
    void hideLoading();
    void showErrorMessage(int errMessage);
}
