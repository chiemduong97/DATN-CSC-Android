package com.example.client.models.product

import java.io.Serializable

class ProductModel(
        var id: Int,
        var name: String,
        var avatar: String,
        var description: String, 
        var price: Double,
        var createdAt: String,
        var updatedAt: String?,
        var category_id: Int,
        var quantity: Int
) : Serializable
