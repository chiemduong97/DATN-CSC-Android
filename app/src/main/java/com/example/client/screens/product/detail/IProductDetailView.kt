package com.example.client.screens.product.detail

import com.example.client.models.product.ProductModel

interface IProductDetailView {
    fun loadData(productModel: ProductModel)
    fun showListProduct(items: List<ProductModel>)
    fun showListEmpty();
    fun showLoading()
    fun hideLoading()
    fun showCart(quantity: Int);
    fun hideCart()
}