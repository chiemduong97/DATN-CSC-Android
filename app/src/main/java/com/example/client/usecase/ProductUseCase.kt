package com.example.client.usecase

import com.example.client.api.ApiClient
import com.example.client.api.service.ProductService
import com.example.client.app.Preferences

class ProductUseCase {
    private val productService by lazy { ApiClient.newInstance().create(ProductService::class.java) }
    companion object {
        fun newInstance() = ProductUseCase()
    }

    fun getProducts(category_id: Int, page: Int, limit: Int) = productService.getByCategory(category_id, page, limit)

    fun filterProducts(query: String, page: Int, limit: Int) = productService.filter(query, page, limit)

    fun getProductsByUrl(url: String, page: Int, limit: Int) = productService.getProductsByUrl(url, page, limit, Preferences.newInstance().profile.id)

}