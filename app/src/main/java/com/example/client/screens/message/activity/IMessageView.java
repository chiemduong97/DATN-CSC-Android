package com.example.client.screens.message.activity;

import com.example.client.models.message.MessengerModel;

import java.util.List;

public interface IMessageView {
    void showMessages(List<MessengerModel> list);
}
