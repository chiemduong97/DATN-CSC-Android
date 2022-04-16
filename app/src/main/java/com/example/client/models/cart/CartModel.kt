package com.example.client.models.cart

import com.example.client.app.Constants
import java.io.Serializable
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class CartModel (var listProduct: ArrayList<CartProductModel>) {
    var order_latitude: Double = 0.0
    var order_longitude: Double = 0.0
    var order_address: String = ""
    var branch_latitude: Double = 0.0
    var branch_longitude: Double = 0.0
    var branch_address: String = ""
    var promotion_code: String? = null
    var promotion_value: Double? = null
    var payment_method: String = Constants.PAYMENT.CASH

    fun getAmount():Double {
        var amount = 0.0
        listProduct.let {
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
        val dLat: Double = (branch_latitude - order_latitude) * (Math.PI / 180)
        val dLon: Double = (branch_longitude - order_longitude) * (Math.PI / 180)
        val la1ToRad: Double = order_latitude * (Math.PI / 180)
        val la2ToRad: Double = branch_latitude * (Math.PI / 180)
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