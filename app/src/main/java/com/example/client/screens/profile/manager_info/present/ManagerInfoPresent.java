package com.example.client.screens.profile.manager_info.present;

import android.graphics.Bitmap;
import android.net.Uri;

import com.example.client.api.ApiClient;
import com.example.client.api.service.UserService;
import com.example.client.app.Firebase;
import com.example.client.app.Preferences;
import com.example.client.dialog.PrimaryDialog;
import com.example.client.models.message.MessageModel;
import com.example.client.models.profile.ProfileModel;
import com.example.client.screens.profile.manager_info.IManagerInfoView;

import com.google.android.gms.common.api.Api;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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
    public void onShowInfoUser() {
        user = Preferences.getInstance().getProfile();
        mView.showInfoUser(user);
    }

    @Override
    public void onUpdateInfo(ProfileModel user) {
        mView.showLoading();
        UserService service = ApiClient.getInstance().create(UserService.class);
        service.updateInfo(user).enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                mView.updateInfo(response.body());
                mView.hideLoading();
            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
                mView.updateInfo(new MessageModel(false,1001,null));
                mView.hideLoading();
            }
        });
    }

    @Override
    public void onUpdatePass(String email, String oldpassword, String newpassword) {
        mView.showLoading();
        UserService service = ApiClient.getInstance().create(UserService.class);
        service.udpatePass(email,oldpassword,newpassword).enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                mView.updatePass(response.body());
                mView.hideLoading();
            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
                mView.updatePass(new MessageModel(false,1001,null));
                mView.hideLoading();
            }
        });
    }

    @Override
    public void onUpdateAvatar(String email, Bitmap avatar) {
        mView.showLoading();
        Firebase.createInstance();
        StorageReference ref = Firebase.getInstance().ref();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        avatar.compress(Bitmap.CompressFormat.JPEG,100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = ref.putBytes(data);
        uploadTask.addOnFailureListener(e ->{
            mView.updateAvatar(new MessageModel(false,1001,null));
            mView.hideLoading();
        }).addOnSuccessListener(taskSnapshot -> uploadTask.continueWithTask(task -> {
            if(!task.isSuccessful()){
                mView.updateAvatar(new MessageModel(false,1001,null));
                mView.hideLoading();
                throw task.getException();
            }
            return ref.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Uri uri = task.getResult();
                UserService service = ApiClient.getInstance().create(UserService.class);
                service.updateAvatar(email,uri.toString()).enqueue(new Callback<MessageModel>() {
                    @Override
                    public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                        user.setAvatar(uri.toString());
                        Preferences.getInstance().setProfile(user);
                        mView.updateAvatar(response.body());
                        mView.hideLoading();
                    }

                    @Override
                    public void onFailure(Call<MessageModel> call, Throwable t) {
                        mView.updateAvatar(new MessageModel(false,1001,null));
                        mView.hideLoading();
                    }
                });
            }
            else {
                mView.updateAvatar(new MessageModel(false,1001,null));
                mView.hideLoading();
            }
        }));

    }
}
