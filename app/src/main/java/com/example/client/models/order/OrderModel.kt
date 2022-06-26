package com.example.client.models.order

import com.example.client.base.BaseModel

data class OrderModel(
        var order_code: String,
        var status: Int,
        var amount: Double,
        var address: String,
        var shipping_fee: Double,
        var promotion_code: String,
        var promotion_value: Double,
        var user_id: Int,
        var branch_id: Int,
        var promotion_id: Int,
        var created_at: String,
        var branch_address: String,
        var phone: String
) {
    fun getTotalPrice() : Double {
        return amount + shipping_fee - promotion_value
    }
    fun isWaiting() = status == 0
    fun isConfirm() = status == 1
    fun isDelivery() = status == 2
    fun isComplete() = status == 3
    fun isDestroy() = status == 4

    fun getStatusString(): String {
        return when {
            isWaiting() -> "Chờ xử lý"
            isConfirm() -> "Đã nhận đơn"
            isDelivery() -> "Đang giao"
            isComplete() -> "Hoàn thành"
            isDestroy() -> "Đã hủy"
            else -> ""
        }
    }

}

data class OrderResponse(
        var order_code: String?,
        var status: Int?,
        var amount: Double?,
        var address: String?,
        var shipping_fee: Double?,
        var promotion_code: String?,
        var promotion_value: Double?,
        var user_id: Int?,
        var branch_id: Int?,
        var promotion_id: Int?,
        var created_at: String?,
        var branch_address: String?,
        var phone: String?
): BaseModel() {
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
            phone = phone.orEmpty()
    )
}

data class OrderRequest(
        var user_id: Int,
        var branch_id: Int,
        var promotion_id: Int? = null,
        var lat: Double,
        var lng: Double,
        var address: String,
        var order_details: List<OrderDetailModel>,
        var branch_lat: Double,
        var branch_lng: Double,
        var branch_address: String,
        var shipping_fee: Double,
        var phone: String
)

fun List<OrderResponse>.toOrders(): List<OrderModel> {
    val orders = arrayListOf<OrderModel>()
    forEach {
        orders.add(it.toOrderModel())
    }
    return orders
}

data class DataOrderResponse(var order_code: String)
data class DataCountOrder(var count: Int)