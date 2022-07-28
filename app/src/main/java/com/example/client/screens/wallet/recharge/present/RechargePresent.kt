package com.example.client.screens.wallet.recharge.present

import com.example.client.app.Constants
import com.example.client.app.Preferences
import com.example.client.app.RxBus
import com.example.client.base.BasePresenterMVP
import com.example.client.models.event.Event
import com.example.client.models.payment.MomoRequest
import com.example.client.screens.wallet.recharge.IRechargeView
import com.example.client.usecase.PaymentUseCase

class RechargePresent(mView: IRechargeView) : BasePresenterMVP<IRechargeView>(mView), IRechargePresent {
    private val paymentUseCase by lazy { PaymentUseCase.newInstance() }
    private val preferences by lazy { Preferences.newInstance() }
    override fun createRequest(amount: Double, customerNumber: String, appData: String) {
        mView?.showLoading()
        subscribe(paymentUseCase.createRecharge(MomoRequest().apply {
            this.user_id = preferences.profile.id
            this.amount = amount
            this.customerNumber = customerNumber
            this.appData = appData
        }),{
            mView?.run {
                hideLoading()
                if (it.is_error) {
                    showErrorMessage(getErrorMessage(it.code))
                    return@subscribe
                }
                rechargeSuccess()
                preferences.profile = preferences.profile.apply { wallet += amount }
                RxBus.newInstance().onNext(Event(Constants.EventKey.RECHARGE_SUCCESS))
            }
        }, {
            it.printStackTrace()
            mView?.run {
                hideLoading()
                showErrorMessage(getErrorMessage(1001))
            }
        })
    }
}