package com.example.client.models.product

import com.example.client.base.BaseModel

data class ProductModel(
        var id: Int,
        var name: String,
        var avatar: String,
        var description: String,
        var price: Double,
        var created_at: String,
        var quantity: Int,
        var category_id: Int,
) : BaseModel()

data class ProductResponse(
        var id: Int?,
        var name: String?,
        var avatar: String?,
        var description: String?,
        var price: Double?,
        var created_at: String?,
        var quantity: Int?,
        var category_id: Int?,
) : BaseModel() {
    fun toProductModel() = ProductModel(
            id = id ?: -1,
            name = name.orEmpty(),
            avatar = avatar.orEmpty(),
            description = description.orEmpty(),
            price = price ?: 0.0,
            created_at = created_at.orEmpty(),
            quantity = quantity ?: 0,
            category_id = category_id ?: -1
    )
}

fun List<ProductResponse>.toProducts(): List<ProductModel> {
    val products = arrayListOf<ProductModel>()
    forEach {
        products.add(it.toProductModel())
    }
    return products
}
