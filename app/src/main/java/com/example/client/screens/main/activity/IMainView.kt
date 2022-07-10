package com.example.client.screens.main.activity

import android.content.Intent
import com.example.client.base.IBaseView
import com.example.client.models.order.OrderModel
import com.example.client.screens.branch.BranchActivity

interface IMainView: IBaseView {
    fun showCart(quantity: Int)
    fun hideCart()
    fun showErrorMessage(errMessage: Int)
    fun showOrderCount(count: Int)
    fun hideOrderCount()
    fun showOrder(order: OrderModel)
    fun hideOrder()
    fun navigateToOrderDetail(orderCode: String)
    fun toBranchScreen()
}