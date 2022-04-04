package com.example.client.screens.wallet.present;

public interface IWalletPresent {
    void onShowInfoUser();
    void onShowTransaction(String label,int user);
    void onShowTransactionByOrderCode(String orderCode);
    void onGetSubject(int subject);
    void onDeleteRecharge(String ordercode);
    void onRequestRecharge(int user, Double amount);
    void onRefeshUserActive(String email);
}
