package com.example.client.models.cart

import com.example.client.app.Constants
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class CartModel(
        var cartProducts: ArrayList<CartProductModel>,
        var order_lat: Double,
        var order_lng: Double,
        var order_address: String,
        var branch_lat: Double,
        var branch_lng: Double,
        var branch_address: String,
        var promotion_id: Int?,
        var promotion_code: String?,
        var promotion_value: Double?,
        var payment_method: Constants.PaymentMethod = Constants.PaymentMethod.COD,
) {
    constructor() : this(
            cartProducts = arrayListOf(),
            order_lat = 0.0,
            order_lng = 0.0,
            order_address = "",
            branch_lat = 0.0,
            branch_lng = 0.0,
            branch_address = "",
            promotion_id = null,
            promotion_code = null,
            promotion_value = null,
            payment_method = Constants.PaymentMethod.COD
    )

    fun getAmount(): Double {
        var amount = 0.0
        cartProducts.let {
            if (it.isNotEmpty()) {
                it.forEach { cartProduct ->
                    amount += cartProduct.product.price.times(cartProduct.quantity)
                }
            }
        }
        return amount
    }


    private fun getDistance(): Double {
        val radius = 6371.0
        val dLat: Double = (branch_lat - order_lat) * (Math.PI / 180)
        val dLon: Double = (branch_lng - order_lng) * (Math.PI / 180)
        val la1ToRad: Double = order_lat * (Math.PI / 180)
        val la2ToRad: Double = branch_lat * (Math.PI / 180)
        val a = sin(dLat / 2) * sin(dLat / 2) + (cos(la1ToRad) * cos(la2ToRad) * sin(dLon / 2) * sin(dLon / 2))
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return radius * c
    }

    private fun getShippingFee(): Double {
        return getDistance() * 5000.0
    }

    fun getShippingFeeExpect(): Double {
        var fee = 0.0
        if (getDistance() > 3) {
            fee = getShippingFee()
        }
        return fee
    }

    fun getTotalPrice(): Double {
        return getAmount() + getShippingFeeExpect() - (promotion_value ?: 0.0)
    }

}