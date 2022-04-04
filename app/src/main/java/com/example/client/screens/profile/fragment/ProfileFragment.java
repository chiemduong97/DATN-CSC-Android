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
import com.example.client.app.Preferences;
import com.example.client.dialog.PrimaryDialog;
import com.example.client.models.banner.BannerModel;
import com.example.client.models.home.HomeIconModel;
import com.example.client.models.profile.ProfileModel;
import com.example.client.models.subject.SubjectModel;
import com.example.client.screens.home.fragment.IHomeView;
import com.example.client.screens.home.present.HomePresent;
import com.example.client.screens.login.activity.LoginEmailActivity;
import com.example.client.screens.message.activity.MessageActivity;
import com.example.client.screens.profile.manager_info.ManagerInfoActivity;
import com.example.client.screens.profile.present.ProfliePresent;
import com.example.client.screens.subject.activity.SubjectByUserActivity;

import java.util.List;

public class ProfileFragment extends Fragment implements IHomeView, IProfileView,View.OnClickListener {
    private TextView tvVersion;
    private HomePresent hPresent;
    private TextView updateInfo,fullname,email;
    private ProfileModel user;
    private ImageView avatar;
    private LinearLayout logout,subjects,contact;
    private PrimaryDialog dialog;
    private SwipeRefreshLayout refreshLayout;
    private ProfliePresent pPresent;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,null);
        tvVersion = view.findViewById(R.id.tvVersion);
        hPresent = new HomePresent(this);
        hPresent.onShowHighLight();
        pPresent = new ProfliePresent(this);
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
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        user = Preferences.getInstance().getProfile();
        fullname.setText(user.getFullname());
        email.setText(user.getEmail());
        Glide.with(this)
                .asBitmap()
                .placeholder(R.drawable.avatar_default)
                .load(user.getAvatar())
                .into(avatar);
    }

    @Override
    public void showIcons(List<HomeIconModel> icons) {

    }

    @Override
    public void showBanners(List<BannerModel> items) {

    }

    @Override
    public void showHighLight(List<SubjectModel> items) {

    }

    @Override
    public void showNew(List<SubjectModel> items) {

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
                Intent intent = new Intent(getContext(), SubjectByUserActivity.class);
                intent.putExtra("user",user.getId());
                startActivity(intent);
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
}
