package com.example.client.screens.order.review.present

import com.example.client.base.IBasePresenter
import com.example.client.models.cart.CartProductModel

interface IReviewOrderPresent: IBasePresenter {
    fun binData()
    fun minus(cartProduct: CartProductModel)
    fun plus(cartProduct: CartProductModel)
    fun createOrder()
}