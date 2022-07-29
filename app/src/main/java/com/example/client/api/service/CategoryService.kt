package com.example.client.api.service

import com.example.client.models.category.CategoryResponse
import com.example.client.models.response.BaseResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface CategoryService {
    @GET("api/category/category_getById.php")
    fun getById(@Query("id") id: Int): Observable<BaseResponse<CategoryResponse>>

    @GET("api/category/category_getLevel_0.php")
    fun getSuperCategories(): Observable<BaseResponse<List<CategoryResponse>>>

    @GET("api/category/category_getLevel_1.php")
    fun getCategories(@Query("category_id") category_id: Int): Observable<BaseResponse<List<CategoryResponse>>>
}