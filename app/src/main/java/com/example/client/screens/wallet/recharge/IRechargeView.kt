package com.example.client.screens.wallet.recharge

import com.example.client.base.IBaseView

interface IRechargeView: IBaseView {
    fun showToast()
    fun showErrorMessage(errorMessage: Int)
    fun rechargeSuccess()
}