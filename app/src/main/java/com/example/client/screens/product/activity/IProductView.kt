package com.example.client.screens.product.activity

import com.example.client.models.cart.CartModel
import com.example.client.models.product.ProductModel

interface IProductView {
    fun showLoading()
    fun hideLoading()
    fun showData(items: List<ProductModel>, cart: CartModel)
    fun showEmptyData()
    fun showErrorMessage(errMessage: Int)
}