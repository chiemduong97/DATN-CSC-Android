package com.example.client.models.order

class OrderModel(
        var ordercode: Int,
        var status: Int,
        var amount: Double,
        var address: String,
        var shippingFee: Double,
        var promotionCode: String?,
        var promotionValue: Double?,
        var user_id: Int,
        var branch_id: Int,
        var promotion_id: Int?
)