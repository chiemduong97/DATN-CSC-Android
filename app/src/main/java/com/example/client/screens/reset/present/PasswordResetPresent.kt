package com.example.client.screens.reset.present;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.widget.TextView;

import com.example.client.R;
import com.example.client.api.ApiClient;
import com.example.client.api.service.UserService;
import com.example.client.app.Constants;
import com.example.client.models.message.MessageModel;
import com.example.client.screens.reset.activity.IPasswordResetView;

import org.jetbrains.annotations.NotNull;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PasswordResetPresent implements IPasswordResetPresent{
    private final IPasswordResetView pView;
    public PasswordResetPresent(IPasswordResetView pView){
        this.pView = pView;
    }

    @Override
    public void verification(String email, String code) {
        pView.showVerifyLoading();
        UserService service = ApiClient.getInstance().create(UserService.class);
        service.verification(email,code).enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NotNull Call<MessageModel> call, @NotNull Response<MessageModel> response) {
                if (response.body() == null) {
                    pView.hideVerifyLoading();
                    return;
                }
                if (response.body().isStatus()) {
                    pView.showViewPassword();
                } else {
                    pView.showErrorMessage(getErrorMessage(response.body().getCode()));
                }
                pView.hideVerifyLoading();
            }

            @Override
            public void onFailure(@NotNull Call<MessageModel> call, @NotNull Throwable t) {
                pView.showErrorMessage(getErrorMessage(1001));
                pView.hideVerifyLoading();
            }
        });

    }

    @Override
    public void resetPass(String email, String password) {
        pView.showResetLoading();
        UserService service = ApiClient.getInstance().create(UserService.class);
        service.resetPassword(email,password).enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NotNull Call<MessageModel> call, @NotNull Response<MessageModel> response) {
                if (response.body() == null) {
                    pView.hideResetLoading();
                    return;
                }
                if (response.body().isStatus()) {
                    pView.onConfirmReset();
                } else {
                    pView.showErrorMessage(getErrorMessage(response.body().getCode()));
                }
                pView.hideResetLoading();
            }

            @Override
            public void onFailure(@NotNull Call<MessageModel> call, @NotNull Throwable t) {
                pView.showErrorMessage(getErrorMessage(1001));
                pView.hideResetLoading();
            }
        });
    }

    @Override
    public void sendRequest(String email) {
        pView.showSendEmailLoading();
        UserService service = ApiClient.getInstance().create(UserService.class);
        service.sendEmail(email, Constants.RequestType.RESET_PASSWORD).enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NotNull Call<MessageModel> call, @NotNull Response<MessageModel> response) {
                if (response.body() == null) {
                    pView.hideSendEmailLoading();
                    return;
                }
                if (response.body().isStatus()) {
                    pView.sendRequestComplete();
                } else {
                    if(response.body().getCode() == 1010)
                        pView.sendRequestComplete();
                    pView.showErrorMessage(getErrorMessage(response.body().getCode()));
                }
                pView.hideSendEmailLoading();
            }

            @Override
            public void onFailure(@NotNull Call<MessageModel> call, @NotNull Throwable t) {
                pView.showErrorMessage(getErrorMessage(1001));
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
                    String timeString = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);
                    tv.setText(MessageFormat.format("Gửi mail xác thực ({0})", timeString));
                    tv.setTextColor(Color.GRAY);
                    tv.setEnabled(false);
                },Throwable::printStackTrace,() -> {
                    tv.setText(R.string.send_email_verification);
                    tv.setTextColor(Color.BLUE);
                    tv.setEnabled(true);
                });
    }

    private int getErrorMessage(int errorCode) {
        int errMessage = -1;
        switch (errorCode) {
            case Constants.ErrorCode.ERROR_1001:
                errMessage = R.string.err_code_1001;
                break;
            case Constants.ErrorCode.ERROR_1002:
                errMessage = R.string.err_code_1002;
                break;
            case Constants.ErrorCode.ERROR_1003:
                errMessage = R.string.err_code_1003;
                break;
            case Constants.ErrorCode.ERROR_1004:
                errMessage = R.string.err_code_1004;
                break;
            case Constants.ErrorCode.ERROR_1005:
                errMessage = R.string.err_code_1005;
                break;
            case Constants.ErrorCode.ERROR_1006:
                errMessage = R.string.err_code_1006;
                break;
            case Constants.ErrorCode.ERROR_1007:
                errMessage = R.string.err_code_1007;
                break;
            case Constants.ErrorCode.ERROR_1008:
                errMessage = R.string.err_code_1008;
                break;
            case Constants.ErrorCode.ERROR_1009:
                errMessage = R.string.err_code_1009;
                break;
            case Constants.ErrorCode.ERROR_1010:
                errMessage = R.string.err_code_1010;
                break;
            case Constants.ErrorCode.ERROR_1011:
                errMessage = R.string.err_code_1011;
                break;
            case Constants.ErrorCode.ERROR_1012:
                errMessage = R.string.err_code_1012;
                break;
            case Constants.ErrorCode.ERROR_1013:
                errMessage = R.string.err_code_1013;
                break;
        }
        return errMessage;
    }
}
