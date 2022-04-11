package com.example.client.screens.order.detail.present

interface IOrderDetailPresent {
    fun getOrderFromService(ordercode: String)
    fun getBranchFromService(id: Int)
    fun getListOrderDetailFromService(ordercode: String)
    fun destroyOrder(ordercode: String, status: Int)
}