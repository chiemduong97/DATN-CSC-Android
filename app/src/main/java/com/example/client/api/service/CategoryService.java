package com.example.client.api.service;

import com.example.client.models.category.CategoryModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CategoryService {
    @GET("views/category/getAll.php")
    Call<List<CategoryModel>> getAll();
    @GET("views/category/getById.php")
    Call<CategoryModel> getById(@Query("id") int id);
}
