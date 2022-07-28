package com.example.client.api.service

import com.example.client.models.payment.MomoRequest
import com.example.client.models.response.BaseResponse
import com.example.client.models.transaction.DataTransactionResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface TransactionService {
    @POST("api/transaction/recharge_create.php")
    fun createRecharge(@Body momoRequest: MomoRequest): Observable<BaseResponse<DataTransactionResponse>>

}