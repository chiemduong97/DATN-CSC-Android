package com.example.client.api.service

import com.example.client.models.message.MessageModel
import com.example.client.models.order.OrderParam
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface OrderService {
    @POST("api/order/order_create.php")
    fun createOrder(@Body orderParam: OrderParam): Call<MessageModel>
}