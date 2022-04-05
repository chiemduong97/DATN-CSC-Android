package com.example.client.screens.home.present;

import com.example.client.api.ApiClient;
import com.example.client.api.service.BannerService;
import com.example.client.api.service.CategoryService;
import com.example.client.api.service.SubjectService;
import com.example.client.models.banner.BannerModel;
import com.example.client.models.category.CategoryModel;
import com.example.client.models.subject.SubjectModel;
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
    public void onShowBanners() {
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
    public void onShowHighLight() {
        SubjectService service = ApiClient.getInstance().create(SubjectService.class);
        service.getHighLight(10).enqueue(new Callback<List<SubjectModel>>() {
            @Override
            public void onResponse(Call<List<SubjectModel>> call, Response<List<SubjectModel>> response) {
                hView.showHighLight(response.body());
            }

            @Override
            public void onFailure(Call<List<SubjectModel>> call, Throwable t) {
                hView.showHighLight(new ArrayList<>());
            }
        });
    }

    @Override
    public void onShowNew() {
        SubjectService service = ApiClient.getInstance().create(SubjectService.class);
        service.getNew(10).enqueue(new Callback<List<SubjectModel>>() {
            @Override
            public void onResponse(Call<List<SubjectModel>> call, Response<List<SubjectModel>> response) {
                hView.showNew(response.body());
            }

            @Override
            public void onFailure(Call<List<SubjectModel>> call, Throwable t) {
                hView.showNew(new ArrayList<>());
            }
        });
    }

}
