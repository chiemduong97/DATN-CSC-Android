package com.example.client.api.service;

import com.example.client.models.banner.BannerModel;
import com.example.client.models.category.CategoryModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BannerService {
    @GET("views/banner/getAll.php")
    Call<List<BannerModel>> getAll();

    @GET("views/nhanvien/getDepartment.php")
    Call<List<String>> getDepartment();

}
