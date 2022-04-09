package com.example.client.models.cart

import com.example.client.models.product.ProductModel

class CartProductModel(var product: ProductModel, var quantity: Int) {
    fun getPrice() : Double{
        return product.price * quantity
    }
}