package com.example.client.screens.order.detail.present

import com.example.client.base.IBasePresenter

interface IOrderDetailPresent: IBasePresenter {
    fun getOrder(orderCode: String)
    fun getOrderDetails(orderCode: String)
    fun destroyOrder(orderCode: String)
}