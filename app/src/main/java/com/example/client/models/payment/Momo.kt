package com.example.client.models.payment

import com.example.client.app.Constants
import com.example.client.base.BaseModel

data class MomoModel(
        val status: Int,
        val message: String,
        val transid: String,
        val amount: Double,
        val signature: String,
) : BaseModel()

data class MomoRequest(
        var user_id: Int? = null,
        var amount: Double? = null,
        var customerNumber: String? = null,
        var appData: String? = null,
        var hash: String? = null
) : BaseModel()

data class MomoResponse(
        val status: Int?,
        val message: String?,
        val transid: String?,
        val amount: Double?,
        val signature: String?,
) : BaseModel() {
    fun toMomoModel() = MomoModel(
            status = status ?: -1,
            message = message.orEmpty(),
            transid = transid.orEmpty(),
            amount = amount ?: 0.0,
            signature = signature.orEmpty()
    )
}
