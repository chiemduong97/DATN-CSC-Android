package com.example.client.usecase

import com.example.client.api.ApiClient
import com.example.client.api.service.CategoryService
import com.example.client.models.category.CategoryResponse
import com.example.client.models.response.BaseResponse
import io.reactivex.Observable

class CategoryUseCase {
    private val categoryService by lazy { ApiClient.getInstance().create(CategoryService::class.java) }
    companion object {
        fun newInstance() = CategoryUseCase()
    }
    fun getParents(): Observable<BaseResponse<List<CategoryResponse>>> {
        return categoryService.parent
    }

}