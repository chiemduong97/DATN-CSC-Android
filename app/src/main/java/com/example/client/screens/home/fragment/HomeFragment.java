package com.example.client.screens.home.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.example.client.models.banner.BannerModel;
import com.example.client.models.branch.BranchModel;
import com.example.client.models.category.CategoryModel;
import com.example.client.models.event.Event;
import com.example.client.models.profile.ProfileModel;
import com.example.client.screens.branch.BranchActivity;
import com.example.client.screens.home.item.BannerItem;
import com.example.client.screens.home.item.HomeCategoryItem;
import com.example.client.screens.home.present.HomePresent;
import com.example.client.screens.product.activity.ProductActivity;
import com.example.client.screens.profile.manager_info.ManagerInfoActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class HomeFragment extends Fragment implements View.OnClickListener, IHomeView {
    private RecyclerView recyclerViewIcon;
    private ViewPager2 pagerBanner;
    private SwipeRefreshLayout refreshLayout;

    private RecyclerView recyclerViewHighLight;
    private TextView moreHighLight;
    private TextView titleHighLight;

    private RecyclerView recyclerViewNew;
    private TextView moreNew;
    private TextView titleNew;
    private TextView wellcome;
    private LinearLayout profile;
    private TabLayout tabDots;
    private ImageView avatar;
    private ImageView banner_empty,high_light_empty,new_empty;
    private TextView tvBranchName, tvBranchAddress, tvOrderAddress;
    private RelativeLayout rllChangeBranch;

    private HomePresent hPresent;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,null);
        recyclerViewIcon = view.findViewById(R.id.recyclerViewIcon);
        pagerBanner = view.findViewById(R.id.pagerBanner);
        refreshLayout = view.findViewById(R.id.container);
        avatar = view.findViewById(R.id.avatar);
        recyclerViewHighLight = view.findViewById(R.id.recyclerViewHightLight);
        moreHighLight = view.findViewById(R.id.moreHightLight);
        titleHighLight = view.findViewById(R.id.titleHightLight);

        recyclerViewNew = view.findViewById(R.id.recyclerViewNew);
        moreNew = view.findViewById(R.id.moreNew);
        titleNew = view.findViewById(R.id.titleNew);
        tabDots = view.findViewById(R.id.tabDots);
        wellcome = view.findViewById(R.id.wellcome);
        profile = view.findViewById(R.id.profile);
        banner_empty = view.findViewById(R.id.banner_empty);
        high_light_empty = view.findViewById(R.id.high_light_empty);
        new_empty = view.findViewById(R.id.new_empty);
        tvBranchName = view.findViewById(R.id.tv_branch_name);
        tvBranchAddress = view.findViewById(R.id.tv_branch_address);
        rllChangeBranch = view.findViewById(R.id.rll_change_branch);
        tvOrderAddress = view.findViewById(R.id.tv_order_address);

        hPresent = new HomePresent(this);

        refreshLayout.setOnRefreshListener(() -> {
            hPresent.getProductsHighLightFromService();
            hPresent.getProductNewFromService();
            hPresent.getBranchFromRes();
            hPresent.getCategoriesFromService();
            hPresent.getListBannerFromService();
            refreshLayout.setRefreshing(false);
        });



        moreHighLight.setOnClickListener(this);
        moreNew.setOnClickListener(this);
        profile.setOnClickListener(this);

        hPresent.getUserFromRes();
        hPresent.getProductsHighLightFromService();
        hPresent.getProductNewFromService();
        hPresent.getBranchFromRes();
        hPresent.getCategoriesFromService();
        hPresent.getListBannerFromService();
        rllChangeBranch.setOnClickListener(this);

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
            case Constants.EventKey.CHANGE_BRANCH:
                hPresent.getBranchFromRes();
                break;
            case Constants.EventKey.UPDATE_PROFILE_AVATAR:
            case Constants.EventKey.UPDATE_LOCATION:
            case Constants.EventKey.UPDATE_PROFILE_INFO:
                hPresent.getUserFromRes();
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
            case R.id.moreHightLight:

                break;
            case R.id.moreNew:

                break;
            case R.id.profile:
                startActivity(new Intent(getActivity(), ManagerInfoActivity.class));
                break;
            case R.id.rll_change_branch:
                startActivity(new Intent(getActivity(), BranchActivity.class));
                break;
        }
    }

    @Override
    public void showCategories(List<CategoryModel> items) {
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2, GridLayoutManager.HORIZONTAL, false);
        recyclerViewIcon.setLayoutManager(manager);
        HomeCategoryItem item = new HomeCategoryItem(getContext(), items, categoryModel -> startActivity(ProductActivity.Companion.newInstance(getContext(),categoryModel, Constants.Method.CATEGORY)));
        recyclerViewIcon.setAdapter(item);
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
    public void showBranchInfo(BranchModel branch) {
        tvBranchName.setText(branch.getName());
        tvBranchAddress.setText(branch.getAddress());
    }

    @Override
    public void showUserInfo(ProfileModel profile) {
        DateFormat format = new SimpleDateFormat("HH", Locale.getDefault());
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
        wellcome.setText(MessageFormat.format("Chào {0}{1}", greet, profile.getFullname()));
        Glide.with(this)
                .asBitmap()
                .placeholder(R.drawable.avatar_default)
                .load(profile.getAvatar())
                .into(avatar);
        tvOrderAddress.setText(profile.getAddress());
    }

    @Override
    public void toBranchScreen() {
        startActivity(new Intent(getActivity(), BranchActivity.class));
    }

}
