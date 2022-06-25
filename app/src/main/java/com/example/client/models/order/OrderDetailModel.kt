package com.example.client.models.order

import com.example.client.base.BaseModel

data class OrderDetailModel(
        var quantity: Int,
        var product_id: Int,
        var price: Double,
        var name: String
) : BaseModel()

data class OrderDetailResponse(
        var quantity: Int?,
        var product_id: Int?,
        var price: Double?,
        var name: String?
) : BaseModel() {
    fun toOrderDetailModel() = OrderDetailModel(
            quantity = quantity ?: 0,
            product_id = product_id ?: -1,
            price = price ?: 0.0,
            name = name.orEmpty()
    )
}

fun List<OrderDetailResponse>.toOrderDetails(): List<OrderDetailModel> {
    val products = arrayListOf<OrderDetailModel>()
    forEach {
        products.add(it.toOrderDetailModel())
    }
    return products
}
