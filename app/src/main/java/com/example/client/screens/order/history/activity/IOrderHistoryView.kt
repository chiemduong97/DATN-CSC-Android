package com.example.client.screens.order.history.activity

import com.example.client.models.order.OrderModel

interface IOrderHistoryView {
    fun showData(list: List<OrderModel>)
    fun showLoading()
    fun hideLoading()
    fun showEmpty()
    fun showErrorMessage(errMessage: Int)
}