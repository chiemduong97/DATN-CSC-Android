package com.example.client.screens.main.activity;

import com.example.client.models.order.OrderModel;

public interface IMainView {
    void showHomeScreen();
    void showNotiScreen();
    void showProfileScreen();
    void showCart(int quantity);
    void hideCart();
    void showLoading();
    void hideLoading();
    void showErrorMessage(int errMessage);
    void showOrderCount(int count);
    void hideOrderCount();
    void showOrder(OrderModel order);
    void hideOrder();
}
