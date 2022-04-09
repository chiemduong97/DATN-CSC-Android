package com.example.client.api.service

import com.example.client.models.product.ProductModel
import com.example.client.models.subject.SubjectModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductService {
    @GET("api/product/product_getByCategory.php")
    fun getByCategory(@Query("category_id") id: Int): Call<List<ProductModel>>
}