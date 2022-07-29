package com.example.client.models.transaction

import com.example.client.app.Constants
import com.example.client.base.BaseModel

data class TransactionModel(
        var transid: String,
        var transid_momo: String?,
        var created_at: String,
        var type: Constants.TransactionType,
        var amount: Double,
        var user_id: Int,
        var status: Int,
        var order_code: String?,
) : BaseModel()

data class TransactionResponse(
        var transid: String?,
        var transid_momo: String?,
        var created_at: String?,
        var type: Constants.TransactionType?,
        var amount: Double?,
        var user_id: Int?,
        var status: Int?,
        var order_code: String?,
) : BaseModel() {
    fun toTransactionModel() = TransactionModel(
            transid = transid.orEmpty(),
            transid_momo = transid_momo,
            created_at = created_at.orEmpty(),
            type = type ?: Constants.TransactionType.PAID,
            amount = amount ?: 0.0,
            user_id = user_id ?: -1,
            status = status ?: 0,
            order_code = order_code
    )
}

fun List<TransactionResponse>.toTransactions() = map { it.toTransactionModel() }

data class DataTransactionResponse(val order_code: String)
