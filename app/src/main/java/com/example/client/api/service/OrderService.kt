package com.example.client.api.service

import com.example.client.models.order.*
import com.example.client.models.response.BaseResponse
import io.reactivex.Observable
import retrofit2.http.*

interface OrderService {
    @POST("api/order/order_create.php")
    fun createOrder(@Body orderRequest: OrderRequest): Observable<BaseResponse<DataOrderResponse>>

    @GET("api/order/order_getByOrderCode.php")
    fun getOrder(@Query("order_code") orderCode: String): Observable<BaseResponse<OrderResponse>>

    @GET("api/order_detail/order_detail_getByOrderCode.php")
    fun getOrderDetails(@Query("order_code") orderCode: String): Observable<BaseResponse<List<OrderDetailResponse>>>

    @GET("api/order/order_getByUser.php")
    fun getOrdersByUser(
            @Query("user_id") user_id: Int,
            @Query("page") page: Int,
            @Query("limit") limit: Int,
    ): Observable<BaseResponse<List<OrderResponse>>>

    @FormUrlEncoded
    @POST("api/order/order_destroy.php")
    fun destroy(@Field("order_code") orderCode: String, @Field("status") status: Int): Observable<BaseResponse<DataOrderResponse>>

    @GET("api/order/order_getCountByUser.php")
    fun getCountOrder(@Query("user_id") user_id: Int): Observable<BaseResponse<DataCountOrder>>
}