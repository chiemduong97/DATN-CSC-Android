package com.example.client.models.order

import com.example.client.R
import com.example.client.app.Constants
import com.example.client.app.Res
import com.example.client.base.BaseModel
import kotlin.math.roundToInt

data class OrderModel(
        val order_code: String,
        var status: Int,
        val amount: Double,
        val address: String,
        val shipping_fee: Double,
        val promotion_code: String,
        val promotion_value: Double,
        val user_id: Int,
        val branch_id: Int,
        val promotion_id: Int,
        val created_at: String,
        val branch_address: String,
        val phone: String,
        val payment_method: Constants.PaymentMethod,
) : BaseModel() {
    fun getTotalPrice() = amount + shipping_fee - if (promotion_value < 1) (amount * promotion_value / 1000).roundToInt() * 1000.0 else promotion_value
    fun isWaiting() = status == 0
    fun isConfirm() = status == 1
    fun isDelivery() = status == 2
    fun isComplete() = status == 3
    fun isDestroy() = status == 4

    fun getStatusString(): String {
        return when {
            isWaiting() -> Res.context?.getString(R.string.order_status_0).orEmpty()
            isConfirm() -> Res.context?.getString(R.string.order_status_1).orEmpty()
            isDelivery() -> Res.context?.getString(R.string.order_status_2).orEmpty()
            isComplete() -> Res.context?.getString(R.string.order_status_3).orEmpty()
            isDestroy() -> Res.context?.getString(R.string.order_status_4).orEmpty()
            else -> ""
        }
    }

}

data class OrderResponse(
        val order_code: String?,
        val status: Int?,
        val amount: Double?,
        val address: String?,
        val shipping_fee: Double?,
        val promotion_code: String?,
        val promotion_value: Double?,
        val user_id: Int?,
        val branch_id: Int?,
        val promotion_id: Int?,
        val created_at: String?,
        val branch_address: String?,
        val phone: String?,
        val payment_method: Constants.PaymentMethod?,
) : BaseModel() {
    fun toOrderModel() = OrderModel(
            order_code = order_code.orEmpty(),
            status = status ?: 0,
            amount = amount ?: 0.0,
            address = address.orEmpty(),
            shipping_fee = shipping_fee ?: 0.0,
            promotion_code = promotion_code.orEmpty(),
            promotion_value = promotion_value ?: 0.0,
            user_id = user_id ?: -1,
            branch_id = branch_id ?: -1,
            promotion_id = promotion_id ?: -1,
            created_at = created_at.orEmpty(),
            branch_address = branch_address.orEmpty(),
            phone = phone.orEmpty(),
            payment_method = payment_method ?: Constants.PaymentMethod.COD
    )
}

data class OrderRequest(
        var user_id: Int,
        var branch_id: Int,
        var promotion_id: Int?,
        var promotion_code: String?,
        var promotion_value: Double?,
        var lat: Double,
        var lng: Double,
        var address: String,
        var order_details: List<OrderDetailModel>,
        var branch_lat: Double,
        var branch_lng: Double,
        var branch_address: String,
        var shipping_fee: Double,
        var phone: String,
        var payment_method: Constants.PaymentMethod,
)

fun List<OrderResponse>.toOrders() = map { it.toOrderModel() }

data class DataOrderResponse(val order_code: String)
data class DataCountOrder(val count: Int)