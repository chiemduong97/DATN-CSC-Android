package com.example.client.usecase

import com.example.client.api.ApiClient
import com.example.client.api.service.ProductService
import com.example.client.models.category.CategoryModel
import com.example.client.models.product.ProductResponse
import com.example.client.models.response.BaseResponse
import io.reactivex.Observable

class ProductUseCase {
    private val productService by lazy { ApiClient.newInstance().create(ProductService::class.java) }
    val categoryModel: CategoryModel? = null
    companion object {
        fun newInstance() = ProductUseCase()
    }

    fun getByCategory(category_id: Int, branch_id: Int, page: Int, limit: Int): Observable<BaseResponse<List<ProductResponse>>> {
        return productService.getByCategory(category_id, branch_id, page, limit)
    }

}