package com.example.client.screens.payment

import com.example.client.app.Constants
import com.example.client.base.IBaseView

interface IPaymentView: IBaseView {
    fun showPaymentMethod(paymentMethod: Constants.PaymentMethod)
    fun showAmount(amount: Double)
}