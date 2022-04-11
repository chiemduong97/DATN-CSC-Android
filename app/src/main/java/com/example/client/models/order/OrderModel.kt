package com.example.client.models.order

class OrderModel(
        var ordercode: String,
        var status: Int,
        var amount: Double,
        var address: String,
        var shippingFee: Double,
        var promotionCode: String?,
        var promotionValue: Double?,
        var user_id: Int,
        var branch_id: Int,
        var promotion_id: Int?,
        var createdAt: String,
        var branch_address: String
) {
    fun getTotalPrice() : Double {
        return amount + shippingFee - (promotionValue ?: 0.0)
    }
    fun isWaiting() = status == 0
    fun isConfirm() = status == 1
    fun isDelivery() = status == 2
    fun isComplete() = status == 3
    fun isDestroy() = status == 4

    fun getStatusString () : String {
        var string = ""
        when {
            isWaiting() -> {
                string = "Chờ xử lý"
            }
            isConfirm() -> {
                string = "Đã nhận đơn"
            }
            isDelivery() -> {
                string = "Đang giao"
            }
            isComplete() -> {
                string = "Hoàn thành"
            }
            isDestroy() -> {
                string = "Đã hủy"
            }
        }
        return string
    }

}