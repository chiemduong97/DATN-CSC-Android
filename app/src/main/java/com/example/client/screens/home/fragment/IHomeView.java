package com.example.client.screens.home.fragment;

import com.example.client.models.banner.BannerModel;
import com.example.client.models.branch.BranchModel;
import com.example.client.models.category.CategoryModel;
import com.example.client.models.profile.ProfileModel;
import com.example.client.models.subject.SubjectModel;

import java.util.List;

public interface IHomeView {
    void showCategories(List<CategoryModel> items);
    void showBanners(List<BannerModel> items);
    void showHighLight(List<SubjectModel> items);
    void showNew(List<SubjectModel> items);
    void showBranchInfo(BranchModel branch);
    void showUserInfo(ProfileModel profile);
}
