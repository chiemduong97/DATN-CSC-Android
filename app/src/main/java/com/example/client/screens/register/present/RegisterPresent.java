package com.example.client.screens.register.present;

import android.annotation.SuppressLint;

import com.example.client.R;
import com.example.client.api.ApiClient;
import com.example.client.api.service.UserService;
import com.example.client.app.Constants;
import com.example.client.app.MyFirebaseService;
import com.example.client.app.Preferences;
import com.example.client.models.message.MessageModel;
import com.example.client.models.profile.ProfileModel;
import com.example.client.screens.register.activity.IRegisterView;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterPresent implements IRegisterPresent{
    private final IRegisterView rView;

    public RegisterPresent(IRegisterView rView) {
        this.rView = rView;
    }
    @SuppressLint("CheckResult")
    @Override
    public void onRegister(String fullname, String phone, String email, String password) {
        MyFirebaseService myFirebaseService =new MyFirebaseService();
        myFirebaseService.getToken().subscribe(o -> {
            UserService service = ApiClient.getInstance().create(UserService.class);
            service.register(fullname, phone, email, password).enqueue(new Callback<MessageModel>() {
                @Override
                public void onResponse(@NotNull Call<MessageModel> call, @NotNull Response<MessageModel> response) {
                    if (response.body() == null) {
                        rView.hideLoading();
                        rView.showErrorMessage(1001);
                        return;
                    }
                    if (response.body().isStatus()) {
                        Preferences.getInstance().setAccessToken((response.body().getAccessToken()));
                        onGetUserActive(email);
                    }
                }

                @Override
                public void onFailure(@NotNull Call<MessageModel> call, @NotNull Throwable t) {
                    rView.showErrorMessage(1001);
                    rView.hideLoading();
                }
            });
        });

    }

    @Override
    public void onGetUserActive(String email) {
        rView.showLoading();
        UserService service = ApiClient.getInstance().create(UserService.class);
        service.getUserByEmail(email).enqueue(new Callback<ProfileModel>() {
            @Override
            public void onResponse(@NotNull Call<ProfileModel> call, @NotNull Response<ProfileModel> response) {
                if (response.body() == null) {
                    rView.hideLoading();
                    rView.showErrorMessage(1001);
                    return;
                }
                Preferences.getInstance().setProfile(response.body());
                String deviceToken = Preferences.getInstance().getDeviceToken();
                onUpdateDeviceToken(email,deviceToken);
            }

            @Override
            public void onFailure(@NotNull Call<ProfileModel> call, @NotNull Throwable t) {
                rView.showErrorMessage(1001);
                rView.hideLoading();
            }
        });
    }

    @Override
    public void onUpdateDeviceToken(String email, String token) {
        rView.showLoading();
        UserService service = ApiClient.getInstance().create(UserService.class);
        service.updateDeviceToken(email,token).enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NotNull Call<MessageModel> call, @NotNull Response<MessageModel> response) {
                if (response.body() == null) {
                    rView.hideLoading();
                    rView.showErrorMessage(1001);
                    return;
                }
                if (response.body().isStatus()) {
                    rView.register();
                } else {
                    rView.showErrorMessage(getErrorMessage(response.body()));
                }
                rView.hideLoading();

            }

            @Override
            public void onFailure(@NotNull Call<MessageModel> call, @NotNull Throwable t) {
                rView.showErrorMessage(1001);
                rView.hideLoading();
            }
        });
    }

    @Override
    public void sendEmail(String email) {
        rView.showLoading();
        UserService service = ApiClient.getInstance().create(UserService.class);
        service.sendEmail(email, Constants.RequestType.REGISTER).enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NotNull Call<MessageModel> call, @NotNull Response<MessageModel> response) {
                if (response.body() == null) {
                    rView.hideLoading();
                    rView.showErrorMessage(1001);
                    return;
                }
                if (response.body().isStatus()) {
                    rView.showVerificationDialog(false);
                } else if (response.body().getCode() == 1010) {
                    rView.showVerificationDialog(true);
                } else {
                    rView.showErrorMessage(getErrorMessage(response.body()));
                }
                rView.hideLoading();
            }

            @Override
            public void onFailure(@NotNull Call<MessageModel> call, @NotNull Throwable t) {
                rView.showErrorMessage(1001);
                rView.hideLoading();
            }
        });
    }

    @Override
    public void verification(String email, String code, String fullname, String phone, String password) {
        rView.showLoading();
        UserService service = ApiClient.getInstance().create(UserService.class);
        service.verification(email,code).enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NotNull Call<MessageModel> call, @NotNull Response<MessageModel> response) {
                if (response.body() == null) {
                    rView.hideLoading();
                    rView.showErrorMessage(1001);
                    return;
                }
                if (response.body().isStatus()) {
                    onRegister(fullname, phone, email, password);
                } else {
                    rView.showErrorMessage(getErrorMessage(response.body()));
                    rView.hideLoading();
                }
            }

            @Override
            public void onFailure(@NotNull Call<MessageModel> call, @NotNull Throwable t) {
                rView.showErrorMessage(1001);
                rView.hideLoading();
            }
        });
    }

    private int getErrorMessage(MessageModel message) {
        int errMessage = -1;
        switch (message.getCode()) {
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
