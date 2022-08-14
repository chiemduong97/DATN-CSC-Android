package com.example.client.screens.order.detail

import com.example.client.base.IBaseView
import com.example.client.models.order.OrderModel

interface IOrderDetailView: IBaseView {
    fun showOrderDetail(order: OrderModel)
    fun showErrorMessage(errMessage: Int)
    fun showReviewOrder(order: OrderModel)
}