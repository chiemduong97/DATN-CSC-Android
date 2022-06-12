package com.example.client.screens.product.activity

import com.example.client.base.IBaseCollectionView
import com.example.client.models.cart.CartModel
import com.example.client.models.product.ProductModel

interface IProductView: IBaseCollectionView {
    fun showData(items: List<ProductModel>, cart: CartModel)
    fun showMoreData(items: List<ProductModel>, cart: CartModel)
    fun showEmptyData()
    fun showErrorMessage(errMessage: Int)
}