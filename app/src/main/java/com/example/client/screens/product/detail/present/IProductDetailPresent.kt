package com.example.client.screens.product.detail.present

import com.example.client.base.IBasePresenter
import com.example.client.models.cart.CartProductModel
import com.example.client.models.product.ProductModel

interface IProductDetailPresent: IBasePresenter {
    fun loadDataByCategory(category_id: Int)
    fun addToCart(cartProduct: CartProductModel)
    fun getCartFromRes()
}