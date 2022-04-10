package com.example.client.api.service

import com.example.client.models.branch.BranchModel
import com.example.client.models.message.MessageModel
import com.example.client.models.order.OrderDetailModel
import com.example.client.models.order.OrderModel
import com.example.client.models.order.OrderParam
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface OrderService {
    @POST("api/order/order_create.php")
    fun createOrder(@Body orderParam: OrderParam): Call<MessageModel>

    @GET("api/order/order_getByOrderCode.php")
    fun getByOrderCode(@Query("ordercode") ordercode: String): Call<OrderModel>
    @GET("api/order_detail/order_detail_getByOrderCode.php")
    fun getListOrderDetail(@Query("ordercode") ordercode: String): Call<List<OrderDetailModel>>
}