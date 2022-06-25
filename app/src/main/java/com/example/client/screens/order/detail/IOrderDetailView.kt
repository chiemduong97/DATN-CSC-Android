package com.example.client.screens.order.detail

import com.example.client.base.IBaseView
import com.example.client.models.branch.BranchModel
import com.example.client.models.order.OrderDetailModel
import com.example.client.models.order.OrderModel

interface IOrderDetailView: IBaseView {
    fun showOrderDetail(order: OrderModel)
    fun showBranch(branch: BranchModel)
    fun showErrorMessage(errMessage: Int)
    fun showProducts(products: List<OrderDetailModel>)
    fun onRefresh()
}