package com.example.client.screens.product.detail

import com.example.client.base.IBaseView
import com.example.client.models.product.ProductModel

interface IProductDetailView: IBaseView {
    fun showListProduct(items: List<ProductModel>)
    fun showEmptyData();
    fun showCart(quantity: Int);
    fun hideCart()
}