package com.example.client.screens.home.present

import com.example.client.base.IBasePresenter
import com.example.client.models.cart.CartProductModel

interface IHomePresent: IBasePresenter {
    fun binData()
    fun getCategories()
    fun getHomeSections()
    fun addToCart(cartProduct: CartProductModel)
}