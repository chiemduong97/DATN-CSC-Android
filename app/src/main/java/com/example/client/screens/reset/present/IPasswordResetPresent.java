package com.example.client.screens.reset.present;

import android.widget.TextView;

public interface IPasswordResetPresent {
    void verification(String email, String code);
    void resetPass(String email, String password);
    void sendRequest(String email);
    void onCountDownTimer(TextView tv,Integer time);
}
