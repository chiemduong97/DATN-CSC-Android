package com.example.client.models.order

import com.example.client.base.BaseModel
import com.example.client.models.cart.CartProductModel
import com.example.client.models.product.ProductModel

data class OrderDetailModel(
        val quantity: Int,
        val price: Double,
        val product: ProductModel,
) : BaseModel()

data class OrderDetailResponse(
        val quantity: Int?,
        val price: Double?,
        val product: ProductModel?,
) : BaseModel() {
    fun toOrderDetailModel() = OrderDetailModel(
            quantity = quantity ?: 0,
            price = price ?: 0.0,
            product = product ?: ProductModel()
    )
}

fun List<OrderDetailResponse>.toOrderDetails() = map { it.toOrderDetailModel() }

fun List<OrderDetailModel>.toCartProducts() = map {
    CartProductModel(ProductModel().apply {
        id = it.product.id
        name = it.product.name
        price = it.price
        addToCart = it.quantity
    }, it.quantity)
}
