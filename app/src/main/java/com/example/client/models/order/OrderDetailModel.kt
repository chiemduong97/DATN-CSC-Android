package com.example.client.models.order

import com.example.client.base.BaseModel
import com.example.client.models.cart.CartProductModel
import com.example.client.models.product.ProductModel

data class OrderDetailModel(
        val quantity: Int,
        val product_id: Int,
        val price: Double,
        val name: String
) : BaseModel()

data class OrderDetailResponse(
        val quantity: Int?,
        val product_id: Int?,
        val price: Double?,
        val name: String?
) : BaseModel() {
    fun toOrderDetailModel() = OrderDetailModel(
            quantity = quantity ?: 0,
            product_id = product_id ?: -1,
            price = price ?: 0.0,
            name = name.orEmpty()
    )
}

fun List<OrderDetailResponse>.toOrderDetails() = map { it.toOrderDetailModel() }

fun List<OrderDetailModel>.toCartProducts() = map {
    CartProductModel(ProductModel().apply {
        id = it.product_id
        name = it.name
        price = it.price
        addToCart = it.quantity
    }, it.quantity)
}
