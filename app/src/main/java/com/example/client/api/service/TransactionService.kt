package com.example.client.api.service

import com.example.client.models.payment.MomoRequest
import com.example.client.models.response.BaseResponse
import com.example.client.models.transaction.DataTransactionResponse
import com.example.client.models.transaction.TransactionResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TransactionService {
    @POST("api/transaction/recharge_create.php")
    fun createRecharge(@Body momoRequest: MomoRequest): Observable<BaseResponse<DataTransactionResponse>>

    @GET("api/transaction/transaction_getAll.php")
    fun getTransactions(
            @Query("user_id") user_id: Int,
            @Query("type") type: String,
            @Query("page") page: Int,
            @Query("limit") limit: Int
    ): Observable<BaseResponse<List<TransactionResponse>>>

}