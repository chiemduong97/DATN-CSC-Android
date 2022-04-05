package com.example.client.screens.reset.activity;

public interface IPasswordResetView {
    void showViewPassword();
    void onConfirmReset();
    void sendRequestComplete();
    void showSendEmailLoading();
    void hideSendEmailLoading();
    void showVerifyLoading();
    void hideVerifyLoading();
    void showResetLoading();
    void hideResetLoading();
    void showErrorMessage(int errMessage);
}
