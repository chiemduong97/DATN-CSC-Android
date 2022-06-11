package com.example.client.screens.order.present

import com.example.client.models.cart.CartProductModel

interface IReviewOrderPresent {
    fun generationCart()
    fun getUserFromRes()
    fun getBranchFromRes()
    fun getCartFromRes()
    fun minus(cartProduct: CartProductModel)
    fun plus(cartProduct: CartProductModel)
    fun createOrder()
}