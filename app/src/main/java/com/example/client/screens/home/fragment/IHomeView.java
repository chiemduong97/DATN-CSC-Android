package com.example.client.screens.home.fragment;

import com.example.client.base.IBaseView;
import com.example.client.models.banner.BannerModel;
import com.example.client.models.branch.BranchModel;
import com.example.client.models.category.CategoryModel;
import com.example.client.models.profile.ProfileModel;

import java.util.List;

public interface IHomeView extends IBaseView {
    void showCategories(List<CategoryModel> items);
    void showBanners(List<BannerModel> items);
    void showBranchInfo(BranchModel branch);
    void showUserInfo(ProfileModel profile);
    void toBranchScreen();
}
