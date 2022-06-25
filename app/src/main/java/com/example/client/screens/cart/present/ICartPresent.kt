package com.example.client.screens.cart.present

import com.example.client.base.IBasePresenter
import com.example.client.models.cart.CartProductModel

interface ICartPresent: IBasePresenter {
    fun bindData()
    fun minus(cartProduct: CartProductModel)
    fun plus(cartProduct: CartProductModel)
}