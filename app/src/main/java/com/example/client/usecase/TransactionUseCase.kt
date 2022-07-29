package com.example.client.usecase

import com.example.client.api.ApiClient
import com.example.client.api.service.TransactionService
import com.example.client.models.payment.MomoRequest

class TransactionUseCase {
    private val transactionService by lazy { ApiClient.newInstance().create(TransactionService::class.java) }
    companion object {
        fun newInstance() = TransactionUseCase()
    }

    fun createRecharge(momoRequest: MomoRequest) = transactionService.createRecharge(momoRequest)

    fun getTransactions(user_id: Int,type: String, page: Int, limit: Int) = transactionService.getTransactions(user_id, type, page, limit)

}