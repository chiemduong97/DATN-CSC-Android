package com.example.client.screens.payment.present

import com.example.client.app.Constants
import com.example.client.base.IBasePresenter

interface IPaymentPresent: IBasePresenter {
    fun getAmount()
    fun getPaymentMethod()
    fun setPaymentMethod(paymentMethod: Constants.PaymentMethod)
}