package com.example.client.screens.payment.present

import com.example.client.app.Constants
import com.example.client.app.Preferences
import com.example.client.app.RxBus
import com.example.client.base.BasePresenterMVP
import com.example.client.models.event.Event
import com.example.client.screens.payment.IPaymentView

class PaymentPresent(mView: IPaymentView): BasePresenterMVP<IPaymentView>(mView), IPaymentPresent {
    private val preferences by lazy { Preferences.newInstance() }

    override fun getAmount() {
        mView?.showAmount(preferences.profile.wallet)
    }

    override fun getPaymentMethod() {
        mView?.showPaymentMethod(preferences.paymentMethod)
    }

    override fun setPaymentMethod(paymentMethod: Constants.PaymentMethod) {
        preferences.paymentMethod = paymentMethod
        RxBus.newInstance().onNext(Event(Constants.EventKey.CHANGE_PAYMENT_METHOD))
        mView?.onBackPress()
    }
}