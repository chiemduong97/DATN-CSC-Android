package com.example.client.usecase

import com.example.client.api.ApiClient
import com.example.client.api.service.TransactionService
import com.example.client.models.payment.MomoRequest

class PaymentUseCase {
    private val paymentService by lazy { ApiClient.newInstance().create(TransactionService::class.java) }
    companion object {
        fun newInstance() = PaymentUseCase()
    }

    fun createRecharge(momoRequest: MomoRequest) = paymentService.createRecharge(momoRequest)

}