package com.example.client.models.order

class OrderParam(
        var user_id: Int?, var branch_id: Int?, var promotion_id: Int?, var latitude: Double?, var longitude: Double?, var address: String?, var listProduct: List<OrderDetailParam>?,
)