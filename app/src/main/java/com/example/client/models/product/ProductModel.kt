package com.example.client.models.product

import com.example.client.base.BaseModel
import com.example.client.models.cart.CartModel

data class ProductModel(
        var id: Int,
        var name: String,
        var avatar: String,
        var description: String,
        var price: Double,
        var created_at: String,
        var quantity: Int,
        var category_id: Int,
        var addToCart: Int,
) : BaseModel() {
    fun checkAddToCart(cart: CartModel) = apply {
        cart.cartProducts.forEach {
            if (it.product.id == id) {
                addToCart = it.quantity
            }
        }
    }

    constructor() : this(
            id = -1,
            name = "",
            avatar = "",
            description = "",
            price = 0.0,
            created_at = "",
            quantity = 0,
            category_id = -1,
            addToCart = 0
    )
}

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
            category_id = category_id ?: -1,
            addToCart = 0
    )
}

fun List<ProductResponse>.toProducts(): List<ProductModel> {
    val products = arrayListOf<ProductModel>()
    forEach {
        products.add(it.toProductModel())
    }
    return products
}

fun List<ProductModel>.checkCart(cart: CartModel): List<ProductModel> {
    return map {
        it.checkAddToCart(cart)
    }
}
