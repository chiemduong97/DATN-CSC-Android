package com.example.client.screens.wallet.fragment;

import com.example.client.models.message.MessageModel;
import com.example.client.models.profile.ProfileModel;
import com.example.client.models.transaction.TransactionModel;

import java.util.List;

public interface IWalletView {
    void showInfoUser(ProfileModel user);
    void showTransaction(List<TransactionModel> items,String method);
    void showTransactionByOrderCode(TransactionModel item);
    void deleteRecharge(MessageModel message);
    void requestRecharge(MessageModel message);
    void refreshUserActive(MessageModel message);
    void showLoading();
    void hideLoading();
}
