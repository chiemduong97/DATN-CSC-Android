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
    private fun isWaiting() = status == 0
    private fun isConfirm() = status == 1
    private fun isDelivery() = status == 2
    private fun isComplete() = status == 3
    private fun isDestroy() = status == 4

    fun getStatusString () : String {
        var string = ""
        when {
            isWaiting() -> {
                string = "Đang chờ nhận đơn"
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