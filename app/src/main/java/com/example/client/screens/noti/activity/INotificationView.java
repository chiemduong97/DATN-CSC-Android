package com.example.client.screens.noti.activity;

import com.example.client.models.noti.Notification;

import java.util.List;

public interface INotificationView {
    void showNotifications(List<Notification> items);
}
