package com.example.client.api.service;

import com.example.client.models.category.CategoryModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CategoryService {
    @GET("api/category/category_getAll.php")
    Call<List<CategoryModel>> getAll();
    @GET("api/category/category_getById.php")
    Call<CategoryModel> getById(@Query("id") int id);
}
