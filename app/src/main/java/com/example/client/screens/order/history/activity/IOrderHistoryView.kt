package com.example.client.screens.order.history.activity

import com.example.client.base.IBaseCollectionView
import com.example.client.models.order.OrderModel

interface IOrderHistoryView: IBaseCollectionView {
    fun showData(items: List<OrderModel>)
    fun showMoreData(items: List<OrderModel>)
    fun showEmptyData()
    fun showErrorMessage(errMessage: Int)
    fun updateData(orderModel: OrderModel)
}