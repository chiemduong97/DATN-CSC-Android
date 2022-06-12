package com.example.client.screens.profile.manager_info;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.client.R;
import com.example.client.app.Constants;
import com.example.client.dialog.PrimaryDialog;
import com.example.client.models.event.Event;
import com.example.client.models.profile.ProfileModel;
import com.example.client.screens.profile.manager_info.present.ManagerInfoPresent;
import com.example.client.screens.profile.manager_info.update.UpdateInfoActivity;
import com.example.client.screens.profile.manager_info.update.UpdatePasswordActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class ManagerInfoActivity extends AppCompatActivity implements View.OnClickListener,IManagerInfoView {
    private ImageView change_avatar,back,avatar;
    private TextView change_info,fullname,email,birthday,phone;
    private LinearLayout change_password;
    private ManagerInfoPresent mPresent;
    private ActivityResultLauncher<Intent> intentActivityResultLauncher;
    private ProfileModel user;
    private CardView cardView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_info);
        change_avatar = findViewById(R.id.change_avatar);
        avatar = findViewById(R.id.avatar);
        back = findViewById(R.id.back);
        change_info = findViewById(R.id.change_info);
        fullname = findViewById(R.id.fullname);
        email = findViewById(R.id.email);
        birthday = findViewById(R.id.birthday);
        phone = findViewById(R.id.phone);
        change_password = findViewById(R.id.change_password);
        cardView = findViewById(R.id.cardView);
        progressBar = findViewById(R.id.progress_bar);

        mPresent = new ManagerInfoPresent(this);

        change_avatar.setOnClickListener(this);
        change_info.setOnClickListener(this);
        change_password.setOnClickListener(this);
        back.setOnClickListener(this);

        mPresent.getUserFromRes();
        intentActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == Activity.RESULT_OK){
                    Intent data = result.getData();
                    try{
                        if(data.getExtras()!=null){
                            Bundle bundle=data.getExtras();
                            Bitmap bitmap=(Bitmap)bundle.get("data");
                            avatar.setImageBitmap(bitmap);
                            mPresent.updateAvatar(user.getEmail(),bitmap);
                        }
                        else {
                            Uri uri=data.getData();
                            Bitmap bitmap= MediaStore.Images.Media.getBitmap(ManagerInfoActivity.this.getContentResolver(),uri);
                            avatar.setImageBitmap(bitmap);
                            mPresent.updateAvatar(user.getEmail(),bitmap);
                        }
                    }catch (Exception ignored){

                    }
                }
            }
        );
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event event) {
        switch (event.getKey()) {
            case Constants.EventKey.UPDATE_PROFILE_INFO:
                mPresent.getUserFromRes();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.change_avatar:
                Intent take=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Intent pick=new Intent(Intent.ACTION_GET_CONTENT);
                pick.setType("image/*");
                Intent intent=Intent.createChooser(pick,"Chọn");
                intent.putExtra(Intent.EXTRA_INITIAL_INTENTS,new Intent[]{take});
                intentActivityResultLauncher.launch(intent);
                break;
            case R.id.change_info:
                startActivity(new Intent(this, UpdateInfoActivity.class));
                break;
            case R.id.change_password:
                startActivity(new Intent(this, UpdatePasswordActivity.class));
                break;
            case R.id.back:
                finish();
                onBackPressed();
        }
    }

    @Override
    public void showUserInfo(ProfileModel user) {
        this.user = user;
        Glide.with(this)
                .asBitmap()
                .placeholder(R.drawable.avatar_default)
                .load(user.getAvatar())
                .into(avatar);
        fullname.setText(user.getFullname());
        email.setText(user.getEmail());
        birthday.setText(user.getBirthday()==null||user.getBirthday().equals("")?"Chưa đặt":user.getBirthday());
        phone.setText(user.getPhone()==null||user.getPhone().equals("")?"Chưa đặt":user.getPhone());
    }

    @Override
    public void updateInfo() {

    }

    @Override
    public void updatePass() {

    }

    @Override
    public void updateAvatar() {
        new PrimaryDialog(() ->null, () -> null)
                .setDescription("Thay đổi avatar thành công")
                .hideBtnCancel()
                .show(getSupportFragmentManager());
        mPresent.getUserFromRes();
    }

    @Override
    public void showLoading() {
        cardView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        cardView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showErrorMessage(int errMessage) {
        new PrimaryDialog(() ->null, () -> null)
                .setDescription(getString(errMessage))
                .hideBtnCancel()
                .show(getSupportFragmentManager());
    }
}