package com.example.client.screens.reset.activity;

import com.example.client.models.message.MessageModel;

public interface IPasswordResetView {
    void showViewPassword(MessageModel message);
    void resetPassword(MessageModel message);
    void sendEmail(MessageModel message);
    void showSendEmailLoading();
    void hideSendEmailLoading();
    void showVertifiLoading();
    void hideVertifiLoading();
    void showResetLoading();
    void hideResetLoading();
}
