package com.example.client.screens.main.activity

import com.example.client.base.IBaseView
import com.example.client.models.order.OrderModel

interface IMainView: IBaseView {
    fun showCart(quantity: Int)
    fun hideCart()
    fun showErrorMessage(errMessage: Int)
    fun showOrderCount(count: Int)
    fun hideOrderCount()
    fun showOrder(order: OrderModel)
    fun hideOrder()
    fun navigateToOrderDetail(orderCode: String)
}