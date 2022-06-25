package com.example.client.screens.main.present

import com.example.client.base.IBasePresenter

interface IMainPresent: IBasePresenter {
    fun setUserActive(email: String)
    fun getCart()
    fun getOrders()
    fun navigateToOrderDetail()
}