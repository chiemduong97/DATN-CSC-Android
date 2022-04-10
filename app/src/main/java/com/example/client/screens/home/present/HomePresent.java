package com.example.client.screens.home.present;

import com.example.client.api.ApiClient;
import com.example.client.api.service.BannerService;
import com.example.client.api.service.CategoryService;
import com.example.client.app.Preferences;
import com.example.client.models.banner.BannerModel;
import com.example.client.models.category.CategoryModel;
import com.example.client.screens.home.fragment.IHomeView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePresent implements IHomePresent {
    private IHomeView hView;

    public HomePresent(IHomeView hView) {
        this.hView = hView;
    }


    @Override
    public void getCategoriesFromService() {
        CategoryService service = ApiClient.getInstance().create(CategoryService.class);
        service.getAll().enqueue(new Callback<List<CategoryModel>>() {
            @Override
            public void onResponse(Call<List<CategoryModel>> call, Response<List<CategoryModel>> response) {
                hView.showCategories(response.body());
            }

            @Override
            public void onFailure(Call<List<CategoryModel>> call, Throwable t) {
                hView.showCategories(new ArrayList<>());
            }
        });
    }

    @Override
    public void getListBannerFromService() {
        BannerService service = ApiClient.getInstance().create(BannerService.class);
        service.getAll().enqueue(new Callback<List<BannerModel>>() {
            @Override
            public void onResponse(Call<List<BannerModel>> call, Response<List<BannerModel>> response) {
                hView.showBanners(response.body());
            }

            @Override
            public void onFailure(Call<List<BannerModel>> call, Throwable t) {
                hView.showBanners(new ArrayList<>());
            }
        });

    }

    @Override
    public void getProductsHighLightFromService() {

    }

    @Override
    public void getProductNewFromService() {

    }

    @Override
    public void getBranchFromRes() {
        hView.showBranchInfo(Preferences.getInstance().getBranch());
    }

    @Override
    public void getUserFromRes() {
        hView.showUserInfo(Preferences.getInstance().getProfile());
    }

}
