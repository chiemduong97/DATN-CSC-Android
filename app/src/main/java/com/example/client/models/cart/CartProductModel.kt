package com.example.client.models.cart

import com.example.client.models.order.OrderDetailModel
import com.example.client.models.product.ProductModel

data class CartProductModel(
        var product: ProductModel,
        var quantity: Int,
) {
    fun getPrice() = product.price * quantity
}

fun List<CartProductModel>.toOrderDetails() = map { OrderDetailModel(it.quantity, it.product.price, it.product) }