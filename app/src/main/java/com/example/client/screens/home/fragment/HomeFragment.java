package com.example.client.screens.home.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.client.R;
import com.example.client.app.Constants;
import com.example.client.app.Preferences;
import com.example.client.models.banner.BannerModel;
import com.example.client.models.home.HomeIconModel;
import com.example.client.models.profile.ProfileModel;
import com.example.client.models.subject.SubjectModel;
import com.example.client.screens.home.item.BannerItem;
import com.example.client.screens.home.item.HomeIconItem;
import com.example.client.screens.home.present.HomePresent;
import com.example.client.screens.profile.manager_info.ManagerInfoActivity;
import com.example.client.screens.subject.activity.SubjectMoreActivity;
import com.example.client.screens.subject.item.SubjectVerticalItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


public class HomeFragment extends Fragment implements View.OnClickListener, IHomeView {
    private RecyclerView recyclerViewIcon;
    private ViewPager2 pagerBanner;
    private SwipeRefreshLayout refreshLayout;

    private RecyclerView recyclerViewHightLight;
    private TextView moreHightLight;
    private TextView titleHightLight;

    private RecyclerView recyclerViewNew;
    private TextView moreNew;
    private TextView titleNew;
    private TextView wellcome;
    private LinearLayout profile;
    private TabLayout tabDots;
    private ImageView avatar;
    private ImageView banner_empty,high_light_empty,new_empty;

    private HomePresent hPresent;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,null);
        recyclerViewIcon = view.findViewById(R.id.recyclerViewIcon);
        pagerBanner = view.findViewById(R.id.pagerBanner);
        refreshLayout = view.findViewById(R.id.container);
        avatar = view.findViewById(R.id.avatar);
        recyclerViewHightLight = view.findViewById(R.id.recyclerViewHightLight);
        moreHightLight = view.findViewById(R.id.moreHightLight);
        titleHightLight = view.findViewById(R.id.titleHightLight);

        recyclerViewNew = view.findViewById(R.id.recyclerViewNew);
        moreNew = view.findViewById(R.id.moreNew);
        titleNew = view.findViewById(R.id.titleNew);
        tabDots = view.findViewById(R.id.tabDots);
        wellcome = view.findViewById(R.id.wellcome);
        profile = view.findViewById(R.id.profile);
        banner_empty = view.findViewById(R.id.banner_empty);
        high_light_empty = view.findViewById(R.id.high_light_empty);
        new_empty = view.findViewById(R.id.new_empty);

        hPresent = new HomePresent(this);

        refreshLayout.setOnRefreshListener(() -> refreshLayout.setRefreshing(false));



        moreHightLight.setOnClickListener(this);
        moreNew.setOnClickListener(this);
        profile.setOnClickListener(this);
        hPresent.onShowIcons();
        hPresent.onShowBanners();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        hPresent.onShowHighLight();
        hPresent.onShowNew();
        ProfileModel user = Preferences.getInstance().getProfile();
        DateFormat format = new SimpleDateFormat("HH");
        int hour = Integer.parseInt(format.format(Calendar.getInstance().getTime()));
        String greet = "";
        if (hour >= 4 && hour < 11)
            greet = "buổi sáng, ";
        else if (hour >= 11 && hour < 13)
            greet = "buổi trưa, ";
        else if (hour >= 13 && hour < 18)
            greet = "buổi chiều, ";
        else
            greet = "buổi tối, ";
        wellcome.setText("Chào " + greet + user.getFullname());
        Glide.with(this)
                .asBitmap()
                .placeholder(R.drawable.avatar_default)
                .load(user.getAvatar())
                .into(avatar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.moreHightLight:
                Intent intentHightLight = new Intent(getActivity(), SubjectMoreActivity.class);
                intentHightLight.putExtra("name",titleHightLight.getText().toString());
                intentHightLight.putExtra("method", Constants.MORE.HIGHLIGHT);
                startActivity(intentHightLight);
                break;
            case R.id.moreNew:
                Intent intentNew = new Intent(getActivity(), SubjectMoreActivity.class);
                intentNew.putExtra("name",titleNew.getText().toString());
                intentNew.putExtra("method", Constants.MORE.NEW);
                startActivity(intentNew);
                break;
            case R.id.profile:
                startActivity(new Intent(getActivity(), ManagerInfoActivity.class));
                break;
        }
    }

    @Override
    public void showIcons(List<HomeIconModel> icons) {
        GridLayoutManager managerIcon = new GridLayoutManager(getContext(),4);
        recyclerViewIcon.setLayoutManager(managerIcon);
        HomeIconItem homeIconItem = new HomeIconItem(getContext(),icons);
        recyclerViewIcon.setAdapter(homeIconItem);
    }

    @Override
    public void showBanners(List<BannerModel> items) {
        if(items == null || items.size() == 0){
            banner_empty.setVisibility(View.VISIBLE);
            pagerBanner.setVisibility(View.GONE);
        }
        else {
            banner_empty.setVisibility(View.GONE);
            pagerBanner.setVisibility(View.VISIBLE);
            BannerItem bannerItem = new BannerItem(getActivity(),items);
            pagerBanner.setAdapter(bannerItem);
            new TabLayoutMediator(tabDots,pagerBanner,(tab, position) -> {

            }).attach();
        }

    }

    @Override
    public void showHighLight(List<SubjectModel> items) {
        if(items == null || items.size() == 0){
            high_light_empty.setVisibility(View.VISIBLE);
            recyclerViewHightLight.setVisibility(View.GONE);
        }
        else {
            high_light_empty.setVisibility(View.GONE);
            recyclerViewHightLight.setVisibility(View.VISIBLE);
            LinearLayoutManager managerHightLight = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
            SubjectVerticalItem subjectVerticalItem = new SubjectVerticalItem(items,getContext());
            recyclerViewHightLight.setLayoutManager(managerHightLight);
            recyclerViewHightLight.setAdapter(subjectVerticalItem);
        }

    }

    @Override
    public void showNew(List<SubjectModel> items) {
        if(items == null || items.size() == 0){
            new_empty.setVisibility(View.VISIBLE);
            recyclerViewNew.setVisibility(View.GONE);
        }
        else {
            new_empty.setVisibility(View.GONE);
            recyclerViewNew.setVisibility(View.VISIBLE);
            LinearLayoutManager managerNew = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
            SubjectVerticalItem subjectVerticalItem = new SubjectVerticalItem(items,getContext());
            recyclerViewNew.setLayoutManager(managerNew);
            recyclerViewNew.setAdapter(subjectVerticalItem);
        }

    }

}
