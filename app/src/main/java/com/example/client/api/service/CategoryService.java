package com.example.client.api.service;

import com.example.client.models.category.CategoryModel;
import com.example.client.models.category.CategoryResponse;
import com.example.client.models.response.BaseResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CategoryService {
    @GET("api/category/category_getById.php")
    Observable<BaseResponse<CategoryResponse>> getById(@Query("id") int id);

    @GET("api/category/category_getLevel_0.php")
    Observable<BaseResponse<List<CategoryResponse>>> getSuperCategories();

    @GET("api/category/category_getLevel_1.php")
    Observable<BaseResponse<List<CategoryResponse>>> getCategories(@Query("category_id") int category_id);


}
