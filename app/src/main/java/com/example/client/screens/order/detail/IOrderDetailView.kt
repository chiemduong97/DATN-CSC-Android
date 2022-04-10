package com.example.client.screens.order.detail

import com.example.client.models.branch.BranchModel
import com.example.client.models.order.OrderDetailModel
import com.example.client.models.order.OrderModel

interface IOrderDetailView {
    fun initView(order: OrderModel)
    fun showBranchOrder(branch: BranchModel)
    fun showLoading()
    fun hideLoading()
    fun showErrorMessage(errMessage: Int)
    fun showListProduct(products: List<OrderDetailModel>)
}