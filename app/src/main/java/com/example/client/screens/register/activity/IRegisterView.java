package com.example.client.screens.register.activity;

import com.example.client.models.message.MessageModel;

public interface IRegisterView {
    void register(MessageModel model);
    void showLoading();
    void hideLoading();
    void showDialog(MessageModel message);
    void toRegister(MessageModel message);
}
