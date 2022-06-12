package com.example.client.screens.profile.manager_info.present;

import android.graphics.Bitmap;
import android.net.Uri;

import com.example.client.R;
import com.example.client.api.ApiClient;
import com.example.client.api.service.UserService;
import com.example.client.app.Constants;
import com.example.client.app.Firebase;
import com.example.client.app.Preferences;
import com.example.client.models.event.Event;
import com.example.client.models.message.MessageModel;
import com.example.client.models.profile.ProfileModel;
import com.example.client.screens.profile.manager_info.IManagerInfoView;

import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagerInfoPresent implements IManagerInfoPresent{
    private IManagerInfoView mView;
    private ProfileModel user;
    public ManagerInfoPresent(IManagerInfoView mView){
        this.mView = mView;
    }
    @Override
    public void getUserFromRes() {
        user = Preferences.newInstance().getProfile();
        mView.showUserInfo(user);
    }

    @Override
    public void updateProfile(ProfileModel user) {
        mView.showLoading();
        UserService service = ApiClient.newInstance().create(UserService.class);
        service.updateInfo(user).enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NotNull Call<MessageModel> call, @NotNull Response<MessageModel> response) {
                if (response.body() == null) {
                    mView.showErrorMessage(getErrorMessage(1001));
                    mView.hideLoading();
                    return;
                }
                if (response.body().isStatus()) {
                    mView.updateInfo();
//                    RxBus.getInstance().onNext(new Event(Constants.EventKey.UPDATE_PROFILE_INFO));
                    EventBus.getDefault().post(new Event(Constants.EventKey.UPDATE_PROFILE_INFO));
                } else {
                    mView.showErrorMessage(response.body().getCode());
                }
                mView.hideLoading();
            }

            @Override
            public void onFailure(@NotNull Call<MessageModel> call, @NotNull Throwable t) {
                mView.showErrorMessage(getErrorMessage(1001));
                mView.hideLoading();
            }
        });
    }

    @Override
    public void updatePassword(String email, String oldpassword, String newpassword) {
        mView.showLoading();
        UserService service = ApiClient.newInstance().create(UserService.class);
        service.udpatePass(email,oldpassword,newpassword).enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NotNull Call<MessageModel> call, @NotNull Response<MessageModel> response) {
                if (response.body() == null) {
                    mView.showErrorMessage(getErrorMessage(1001));
                    mView.hideLoading();
                    return;
                }
                if (response.body().isStatus()) {
                    mView.updatePass();
                } else {
                    mView.showErrorMessage(response.body().getCode());
                }
                mView.hideLoading();
            }

            @Override
            public void onFailure(@NotNull Call<MessageModel> call, @NotNull Throwable t) {
                mView.showErrorMessage(getErrorMessage(1001));
                mView.hideLoading();
            }
        });
    }

    @Override
    public void updateAvatar(String email, Bitmap avatar) {
        mView.showLoading();
        Firebase.createInstance();
        StorageReference ref = Firebase.getInstance().ref();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        avatar.compress(Bitmap.CompressFormat.JPEG,100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = ref.putBytes(data);
        uploadTask.addOnFailureListener(e ->{
            mView.showErrorMessage(getErrorMessage(1001));
            mView.hideLoading();
        }).addOnSuccessListener(taskSnapshot -> uploadTask.continueWithTask(task -> {
            if(!task.isSuccessful()){
                mView.showErrorMessage(getErrorMessage(1001));
                mView.hideLoading();
                throw task.getException();
            }
            return ref.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Uri uri = task.getResult();
                UserService service = ApiClient.newInstance().create(UserService.class);
                service.updateAvatar(email,uri.toString()).enqueue(new Callback<MessageModel>() {
                    @Override
                    public void onResponse(@NotNull Call<MessageModel> call, @NotNull Response<MessageModel> response) {
                        if (response.body() == null) {
                            mView.showErrorMessage(getErrorMessage(1001));
                            mView.hideLoading();
                            return;
                        }
                        if (response.body().isStatus()) {
                            user.setAvatar(uri.toString());
                            Preferences.newInstance().setProfile(user);
                            mView.updateAvatar();
                            EventBus.getDefault().post(new Event(Constants.EventKey.UPDATE_PROFILE_AVATAR));
                        } else {
                            mView.showErrorMessage(response.body().getCode());
                        }
                        mView.hideLoading();
                    }

                    @Override
                    public void onFailure(@NotNull Call<MessageModel> call, @NotNull Throwable t) {
                        mView.showErrorMessage(getErrorMessage(1001));
                        mView.hideLoading();
                    }
                });
            }
            else {
                mView.showErrorMessage(getErrorMessage(1001));
                mView.hideLoading();
            }
        }));

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
