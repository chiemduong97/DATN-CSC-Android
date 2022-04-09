package com.example.client.screens.main.activity;

public interface IMainView {
    void showHomeScreen();
    void showNotiScreen();
    void showProfileScreen();
    void showCart(int quantity);
    void hideCart();
}
