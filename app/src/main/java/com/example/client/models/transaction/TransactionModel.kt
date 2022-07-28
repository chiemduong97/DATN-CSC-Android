package com.example.client.models.transaction

data class TransactionModel(
        var id: Int,
        var user: Int,
        var subject: Int,
        var ordercode: String?,
        var amount: Double?,
        var createAt: String?,
        var status: Int)

data class DataTransactionResponse(val order_code: String)
