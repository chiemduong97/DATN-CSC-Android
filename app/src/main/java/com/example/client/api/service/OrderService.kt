package com.example.client.api.service

import com.example.client.models.message.MessageModel
import com.example.client.models.order.OrderDetailModel
import com.example.client.models.order.OrderModel
import com.example.client.models.order.OrderParam
import retrofit2.Call
import retrofit2.http.*

interface OrderService {
    @POST("api/order/order_create.php")
    fun createOrder(@Body orderParam: OrderParam): Call<MessageModel>

    @GET("api/order/order_getByOrderCode.php")
    fun getByOrderCode(@Query("ordercode") ordercode: String): Call<OrderModel>
    @GET("api/order_detail/order_detail_getByOrderCode.php")
    fun getListOrderDetail(@Query("ordercode") ordercode: String): Call<List<OrderDetailModel>>

    @GET("api/order/order_getByUser.php")
    fun getByUser(@Query("user_id") user_id: Int): Call<List<OrderModel>>

    @FormUrlEncoded
    @POST("api/order/order_destroy.php")
    fun destroy(@Field("ordercode") ordercode: String, @Field("status") status: Int): Call<MessageModel>
}