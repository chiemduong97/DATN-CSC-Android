package com.example.client.screens.order.review.present

import com.example.client.base.IBasePresenter
import com.example.client.models.cart.CartProductModel

interface IReviewOrderPresent: IBasePresenter {
    fun binData()
    fun minus(cartProduct: CartProductModel)
    fun plus(cartProduct: CartProductModel)
    fun createOrderWithMomo(customerNumber: String, appData: String)
    fun createOrderWithWallet()
    fun createOrder()
    fun removePromotion()
    fun requestMomo()
}