package com.example.client.models.notify

import com.example.client.app.Constants.NotifyAction.ORDER_COMPLETE
import com.example.client.app.Constants.NotifyAction.ORDER_DELIVERY
import com.example.client.app.Constants.NotifyAction.ORDER_DESTROY
import com.example.client.app.Constants.NotifyAction.ORDER_RECEIVED
import com.example.client.app.Constants.NotifyAction.ORDER_SUCCESS
import com.example.client.app.Constants.NotifyAction.RECHARGE_SUCCESS
import com.example.client.base.BaseModel

data class NotifyModel(
        val id: Int = -1,
        val action: String = "",
        val description: String = "",
        val created_at: String = "",
        val user_id: Int? = -1,
        val order_code: String? = "",
) : BaseModel()

data class NotifyResponse(
        val id: Int?,
        val action: String?,
        val description: String?,
        val created_at: String?,
        val user_id: Int?,
        val order_code: String?,
) {
    fun toNotify() = NotifyModel(
            id = id ?: -1,
            action = action.orEmpty(),
            description = description.orEmpty(),
            created_at = created_at.orEmpty(),
            user_id = user_id,
            order_code = order_code
    )
}

fun List<NotifyResponse>.toNotifies() = map { it.toNotify() }



fun getAction(notify: NotifyModel) = when (notify.action) {
    ORDER_SUCCESS -> "Đặt hàng thành công mã đơn #${notify.order_code}"
    ORDER_RECEIVED -> "Đã nhận đơn hàng #${notify.order_code}"
    ORDER_DELIVERY -> "Đơn hàng #${notify.order_code} đang giao"
    ORDER_COMPLETE -> "Đơn hàng #${notify.order_code} đã hoàn thành"
    ORDER_DESTROY -> "Đã hủy đơn hàng  #${notify.order_code}"
    RECHARGE_SUCCESS -> "Nạp tiền thành công"
    else -> "Thông báo từ CSC"
}