package com.example.client.screens.profile.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.client.BuildConfig;
import com.example.client.R;
import com.example.client.app.Constants;
import com.example.client.dialog.PrimaryDialog;
import com.example.client.models.event.Event;
import com.example.client.models.profile.ProfileModel;
import com.example.client.screens.login.activity.LoginEmailActivity;
import com.example.client.screens.message.activity.MessageActivity;
import com.example.client.screens.profile.manager_info.ManagerInfoActivity;
import com.example.client.screens.profile.present.ProfilePresent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ProfileFragment extends Fragment implements  IProfileView,View.OnClickListener {
    private TextView tvVersion;
    private TextView updateInfo,fullname,email;
    private ImageView avatar;
    private LinearLayout logout,subjects,contact;
    private PrimaryDialog dialog;
    private SwipeRefreshLayout refreshLayout;
    private ProfilePresent pPresent;
    private ProfileModel user;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,null);
        tvVersion = view.findViewById(R.id.tvVersion);
        pPresent = new ProfilePresent(this);
        tvVersion.append(BuildConfig.VERSION_NAME);
        updateInfo = view.findViewById(R.id.updateInfo);
        fullname = view.findViewById(R.id.fullname);
        email = view.findViewById(R.id.email);
        updateInfo.setOnClickListener(this);
        avatar = view.findViewById(R.id.avatar);
        logout = view.findViewById(R.id.logout);
        subjects = view.findViewById(R.id.subjects);
        contact = view.findViewById(R.id.contact);
        refreshLayout = view.findViewById(R.id.container);
        refreshLayout.setOnRefreshListener(()-> refreshLayout.setRefreshing(false));
        dialog = new PrimaryDialog();
        dialog.getInstance(getContext());
        subjects.setOnClickListener(this);
        logout.setOnClickListener(this);
        contact.setOnClickListener(this);

        pPresent.getUserFromRes();
        return view;
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
            case Constants.EventKey.UPDATE_PROFILE_AVATAR:
            case Constants.EventKey.UPDATE_PROFILE_INFO:
                pPresent.getUserFromRes();
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
            case R.id.updateInfo:
                startActivity(new Intent(getContext(), ManagerInfoActivity.class));
                break;
            case R.id.logout:
                dialog.setDescription("Bạn có muốn đăng xuất?");
                dialog.setOKListener(()-> pPresent.onLogout());
                dialog.setCancelListener(()->{});
                dialog.show();
                break;
            case R.id.subjects:

                break;
            case R.id.contact:
                Intent i = new Intent(getContext(), MessageActivity.class);
                i.putExtra("user",user.getId());
                startActivity(i);
                break;
        }
    }

    @Override
    public void logout(boolean isSuccess) {
        if(isSuccess) {
            startActivity(new Intent(getActivity(), LoginEmailActivity.class));
        }
        else {
            Toast.makeText(getActivity(),"Đã xảy ra lỗi!",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showUserInfo(ProfileModel profile) {
        this.user = profile;
        fullname.setText(profile.getFullname());
        email.setText(profile.getEmail());
        Glide.with(this)
                .asBitmap()
                .placeholder(R.drawable.avatar_default)
                .load(profile.getAvatar())
                .into(avatar);
    }
}
