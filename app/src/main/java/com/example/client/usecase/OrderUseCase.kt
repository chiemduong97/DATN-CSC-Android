package com.example.client.usecase

import com.example.client.api.ApiClient
import com.example.client.api.service.OrderService
import com.example.client.models.loading.LoadingMode
import com.example.client.models.order.*
import com.example.client.models.response.BaseResponse
import io.reactivex.Observable

class OrderUseCase {
    private val orderService by lazy { ApiClient.newInstance().create(OrderService::class.java) }

    companion object {
        fun newInstance() = OrderUseCase()
    }

    fun getOrders(user_id: Int, page: Int, limit: Int): Observable<BaseResponse<List<OrderResponse>>> {
        return orderService.getOrdersByUser(user_id, page, limit)
    }

    fun createOrder(orderRequest: OrderRequest): Observable<BaseResponse<DataOrderResponse>> {
        return orderService.createOrder(orderRequest)
    }

    fun getOrder(orderCode: String): Observable<BaseResponse<OrderResponse>> {
        return orderService.getOrder(orderCode)
    }

    fun destroyOrder(orderCode: String, status: Int): Observable<BaseResponse<DataOrderResponse>> {
        return orderService.destroy(orderCode, status)
    }

    fun getCountOrder(user_id: Int): Observable<BaseResponse<DataCountOrder>> {
        return orderService.getCountOrder(user_id)
    }


}