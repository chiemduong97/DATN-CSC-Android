package com.example.client.screens.wallet.recharge.present

import com.example.client.base.IBasePresenter

interface IRechargePresent : IBasePresenter {
    fun createRequest(amount: Double, customerNumber: String, appData: String)
}