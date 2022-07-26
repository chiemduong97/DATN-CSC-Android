package com.example.client.screens.product.detail.present

import com.example.client.base.IBasePresenter
import com.example.client.models.cart.CartProductModel
import com.example.client.models.category.CategoryModel

interface IProductDetailPresent: IBasePresenter {
    fun loadDataByCategory(categoryModel: CategoryModel)
    fun addToCart(cartProduct: CartProductModel)
    fun getCartFromRes()
}