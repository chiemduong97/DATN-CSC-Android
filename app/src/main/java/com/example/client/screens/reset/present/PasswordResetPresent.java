package com.example.client.screens.reset.present;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.widget.TextView;

import com.example.client.api.ApiClient;
import com.example.client.api.service.UserService;
import com.example.client.models.message.MessageModel;
import com.example.client.screens.reset.activity.IPasswordResetView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PasswordResetPresent implements IPasswordResetPresent{
    private IPasswordResetView pView;
    public PasswordResetPresent(IPasswordResetView pView){
        this.pView = pView;
    }

    @Override
    public void onVertification(String email, String code) {
        pView.showVertifiLoading();
        UserService service = ApiClient.getInstance().create(UserService.class);
        service.vertification(email,code).enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                pView.verification(response.body());
                pView.hideVertifiLoading();
            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
                pView.verification(new MessageModel(false,1001,null));
                pView.hideVertifiLoading();
            }
        });

    }

    @Override
    public void onResetPassword(String email, String password) {
        pView.showResetLoading();
        UserService service = ApiClient.getInstance().create(UserService.class);
        service.resetPassword(email,password).enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                pView.resetPassword(response.body());
                pView.hideResetLoading();
            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
                pView.resetPassword(new MessageModel(false,1001,null));
                pView.hideResetLoading();
            }
        });
    }

    @Override
    public void onSendEmail(String email) {
        pView.showSendEmailLoading();
        UserService service = ApiClient.getInstance().create(UserService.class);
        service.sendEmail(email).enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                pView.sendEmail(response.body());
                pView.hideSendEmailLoading();
            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
                pView.sendEmail(new MessageModel(false,1001,null));
                pView.hideSendEmailLoading();
            }
        });
    }

    @SuppressLint("CheckResult")
    @Override
    public void onCountDownTimer(TextView tv, Integer time) {
        Observable.interval(1, TimeUnit.SECONDS)
                .take(time)
                .map(v -> v + 1 )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value->{
                    value = time - value;
                    int minutes = (int) ((value % 3600) / 60);
                    int seconds = (int) (value % 60);
                    String timeString = String.format("%02d:%02d", minutes, seconds);
                    tv.setText("Gửi mail xác thực (" + timeString + ")");
                    tv.setTextColor(Color.GRAY);
                    tv.setEnabled(false);
                },Throwable::printStackTrace,() -> {
                    tv.setText("Gửi mail xác thực");
                    tv.setTextColor(Color.BLUE);
                    tv.setEnabled(true);
                });
    }
}
