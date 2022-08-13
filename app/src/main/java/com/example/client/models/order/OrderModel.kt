package com.example.client.models.order

import com.example.client.R
import com.example.client.app.Constants
import com.example.client.app.Res
import com.example.client.base.BaseModel
import com.example.client.models.branch.BranchModel
import com.example.client.models.profile.ProfileModel
import com.example.client.models.promotion.PromotionModel
import com.example.client.models.rating.RatingModel
import kotlin.math.roundToInt

data class OrderModel(
        val order_code: String = "",
        var status: Int = 0,
        val amount: Double = 0.0,
        val address: String = "",
        val shipping_fee: Double = 0.0,
        val created_at: String = "",
        val payment_method: Constants.PaymentMethod = Constants.PaymentMethod.COD,
        val user: ProfileModel = ProfileModel(),
        val branch: BranchModel = BranchModel(),
        val rating: RatingModel? = null,
        val order_details: List<OrderDetailModel> = arrayListOf(),
        val promotion: PromotionModel = PromotionModel(),
) : BaseModel() {
    fun getTotalPrice() = amount + shipping_fee - if (promotion.value < 1) (amount * promotion.value / 1000).roundToInt() * 1000.0 else promotion.value
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
        val created_at: String?,
        val phone: String?,
        val payment_method: Constants.PaymentMethod?,
        val user: ProfileModel?,
        val branch: BranchModel?,
        val rating: RatingModel?,
        val order_details: List<OrderDetailModel>?,
        val promotion: PromotionModel?,
) : BaseModel() {
    fun toOrderModel() = OrderModel(
            order_code = order_code.orEmpty(),
            status = status ?: 0,
            amount = amount ?: 0.0,
            address = address.orEmpty(),
            shipping_fee = shipping_fee ?: 0.0,
            created_at = created_at.orEmpty(),
            payment_method = payment_method ?: Constants.PaymentMethod.COD,
            user = user ?: ProfileModel(),
            branch = branch ?: BranchModel(),
            rating = rating,
            order_details = order_details.orEmpty(),
            promotion = promotion ?: PromotionModel()
    )
}

data class OrderRequest(
        var user_id: Int,
        var branch_id: Int,
        var promotion_id: Int?,
        var address: String,
        var order_details: List<OrderDetailModel>,
        var shipping_fee: Double,
        var phone: String,
        var distance: Double,
        var payment_method: Constants.PaymentMethod,
        var customerNumber: String? = null,
        var appData: String? = null,
        var amount: Double? = null,
)

fun List<OrderResponse>.toOrders() = map { it.toOrderModel() }

data class DataOrderResponse(val order_code: String)
data class DataCountOrder(val count: Int)