package com.example.client.screens.cart.present

import com.example.client.models.cart.CartProductModel

interface ICartPresent {
    fun getBranchFromRes();
    fun getUserFromRes();
    fun getCartFromRes();
    fun minus(cartProduct: CartProductModel)
    fun plus(cartProduct: CartProductModel)
}