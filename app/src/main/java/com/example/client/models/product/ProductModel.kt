package com.example.client.models.product

import com.example.client.base.BaseModel
import com.example.client.models.cart.CartModel
import com.example.client.models.category.CategoryModel
import com.example.client.models.category.HomeSectionModel

data class ProductModel(
        var id: Int = -1,
        var name: String = "",
        val avatar: String = "",
        val description: String = "",
        var price: Double = 0.0,
        val created_at: String = "",
        val quantity: Int = 0,
        var addToCart: Int = 0,
        var category: CategoryModel = CategoryModel()
) : BaseModel() {
    fun checkAddToCart(cart: CartModel) = apply {
        cart.cartProducts.forEach {
            if (it.product.id == id) {
                addToCart = it.quantity
            }
        }
    }
}

data class ProductResponse(
        val id: Int?,
        val name: String?,
        val avatar: String?,
        val description: String?,
        val price: Double?,
        val created_at: String?,
        val quantity: Int?,
        val category: CategoryModel?
) : BaseModel() {
    fun toProductModel() = ProductModel(
            id = id ?: -1,
            name = name.orEmpty(),
            avatar = avatar.orEmpty(),
            description = description.orEmpty(),
            price = price ?: 0.0,
            created_at = created_at.orEmpty(),
            quantity = quantity ?: 0,
            category = category ?: CategoryModel(),
            addToCart = 0,
    )
}

fun List<ProductResponse>.toProducts() = map { it.toProductModel() }

fun List<ProductResponse>.applyHomeSection(homeSection: HomeSectionModel) = map { it.toProductModel().apply { category = homeSection } }


fun List<ProductModel>.checkCart(cart: CartModel) = map { it.checkAddToCart(cart) }
