package com.example.client.screens.reset.present;

import android.widget.TextView;

public interface IPasswordResetPresent {
    void onVertification(String email, String code);
    void onResetPassword(String email, String password);
    void onSendEmail(String email);
    void onCountDownTimer(TextView tv,Integer time);
}
