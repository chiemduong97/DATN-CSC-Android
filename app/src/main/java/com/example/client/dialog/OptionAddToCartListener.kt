package com.example.client.dialog

import com.example.client.models.cart.CartProductModel
import com.example.client.models.category.CategoryModel
import com.example.client.models.product.ProductModel


interface OptionAddToCartListener {
    fun addToCart(cartProduct: CartProductModel)
    fun showProductDetail(category: CategoryModel, product: ProductModel)
}