package com.example.client.api.service

import com.example.client.models.product.ProductModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductService {
    @GET("api/product/product_getByCategory.php")
    fun getByCategory(@Query("category_id") category_id: Int,@Query("branch_id") branch_id: Int ): Call<List<ProductModel>>
}