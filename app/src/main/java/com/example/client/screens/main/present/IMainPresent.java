package com.example.client.screens.main.present;

import com.example.client.models.cart.CartModel;

public interface IMainPresent {
    void replaceFragment(int id);
    void onSetUserActive();
    void getCartFromRes();
    void getListOrderFromService();
}
