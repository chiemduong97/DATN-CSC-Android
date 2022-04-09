package com.example.client.screens.login.present;

import android.annotation.SuppressLint;

import com.example.client.R;
import com.example.client.api.ApiClient;
import com.example.client.api.service.UserService;
import com.example.client.app.Constants;
import com.example.client.app.MyFirebaseService;
import com.example.client.app.Preferences;
import com.example.client.models.message.MessageModel;
import com.example.client.models.profile.ProfileModel;
import com.example.client.screens.login.activity.ILoginView;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPresent implements ILoginPresent{
    private final ILoginView lView;

    public LoginPresent(ILoginView lView){
        this.lView = lView;
    }
    @Override
    public void checkEmail(String email) {
        lView.showLoading();
        UserService service = ApiClient.getInstance().create(UserService.class);
        service.checkEmail(email).enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NotNull Call<MessageModel> call, @NotNull Response<MessageModel> response) {
                if (response.body() == null) {
                    lView.showErrorMessage(getErrorMessage(1001));;
                    lView.hideLoading();
                    return;
                }
                if (response.body().isStatus()) {
                    lView.next();
                } else {
                    lView.showErrorMessage(getErrorMessage(response.body().getCode()));
                    lView.hideLoading();
                }
            }

            @Override
            public void onFailure(@NotNull Call<MessageModel> call, @NotNull Throwable t) {
                lView.showErrorMessage(getErrorMessage(1001));;
                lView.hideLoading();
            }
        });
    }

    @SuppressLint("CheckResult")
    @Override
    public void onLogin(String email,String password) {
        lView.showLoading();
        MyFirebaseService myFirebaseService = new MyFirebaseService();
        myFirebaseService.getToken().subscribe(o -> {
            UserService service = ApiClient.getInstance().create(UserService.class);
            service.login(email,password).enqueue(new Callback<MessageModel>() {
                @Override
                public void onResponse(@NotNull Call<MessageModel> call, @NotNull Response<MessageModel> response) {
                    if (response.body() == null) {
                        lView.showErrorMessage(getErrorMessage(1001));;
                        lView.hideLoading();
                        return;
                    }
                    if (response.body().isStatus()) {
                        Preferences.getInstance().setAccessToken(response.body().getAccessToken());
                        setUserActive(email);
                    } else {
                        lView.showErrorMessage(getErrorMessage(response.body().getCode()));
                        lView.hideLoading();
                    }

                }

                @Override
                public void onFailure(@NotNull Call<MessageModel> call, @NotNull Throwable t) {
                    lView.showErrorMessage(getErrorMessage(1001));;
                    lView.hideLoading();
                }
            });
        });
    }
    @Override
    public void setUserActive(String email){
        UserService service = ApiClient.getInstance().create(UserService.class);
        service.getUserByEmail(email).enqueue(new Callback<ProfileModel>() {
            @Override
            public void onResponse(@NotNull Call<ProfileModel> call, @NotNull Response<ProfileModel> response) {
                if (response.body() == null) {
                    lView.showErrorMessage(getErrorMessage(1001));;
                    lView.hideLoading();
                    return;
                }
                Preferences.getInstance().setProfile(response.body());
                String deviceToken = Preferences.getInstance().getDeviceToken();
                onUpdateDeviceToken(email,deviceToken);
            }

            @Override
            public void onFailure(@NotNull Call<ProfileModel> call, @NotNull Throwable t) {
                lView.showErrorMessage(getErrorMessage(1001));;
                lView.hideLoading();
            }
        });
    }

    @Override
    public void onUpdateDeviceToken(String email, String token) {
        UserService service = ApiClient.getInstance().create(UserService.class);
        service.updateDeviceToken(email,token).enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NotNull Call<MessageModel> call, @NotNull Response<MessageModel> response) {
                if (response.body() == null) {
                    lView.showErrorMessage(getErrorMessage(1001));;
                    lView.hideLoading();
                    return;
                }
                if (response.body().isStatus()) {
                    lView.login();
                } else {
                    lView.showErrorMessage(getErrorMessage(response.body().getCode()));
                }
                lView.hideLoading();

            }

            @Override
            public void onFailure(@NotNull Call<MessageModel> call, @NotNull Throwable t) {
                lView.showErrorMessage(getErrorMessage(1001));;
                lView.hideLoading();
            }
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
