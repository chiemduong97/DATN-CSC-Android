package com.example.client.api.service

import com.example.client.models.product.ProductResponse
import com.example.client.models.response.BaseResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductService {
    @GET("api/product/product_getAll.php")
    fun getByCategory(
            @Query("category_id") category_id: Int,
            @Query("page") page: Int,
            @Query("limit") limit: Int,
    ): Observable<BaseResponse<List<ProductResponse>>>

    @GET("api/product/product_search.php")
    fun filter(
            @Query("query") query: String,
            @Query("page") page: Int,
            @Query("limit") limit: Int,
    ): Observable<BaseResponse<List<ProductResponse>>>
}