package com.example.client.screens.product.activity

import com.example.client.models.product.ProductModel

interface IProductView {
    fun showLoading()
    fun hideLoading()
    fun showData(items: List<ProductModel>)
    fun showEmptyData()
    fun showErrorMessage(errMessage: Int)
}