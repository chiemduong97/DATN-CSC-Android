package com.example.client.screens.product.detail.present

import com.example.client.models.cart.CartProductModel
import com.example.client.models.product.ProductModel

interface IProductDetailPresent {
    fun loadDataByCategory(category_id: Int)
    fun addToCart(cartProduct: CartProductModel)
    fun getCartFromRes()
    fun plus(product: ProductModel)
    fun minus(product: ProductModel)
}