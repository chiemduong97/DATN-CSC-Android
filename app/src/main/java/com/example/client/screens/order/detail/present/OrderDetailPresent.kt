package com.example.client.screens.order.detail.present

import com.example.client.app.Constants
import com.example.client.app.RxBus
import com.example.client.base.BasePresenterMVP
import com.example.client.models.event.Event
import com.example.client.models.event.ValueEvent
import com.example.client.models.order.OrderModel
import com.example.client.screens.order.detail.IOrderDetailView
import com.example.client.usecase.OrderUseCase

class OrderDetailPresent(mView: IOrderDetailView) : BasePresenterMVP<IOrderDetailView>(mView), IOrderDetailPresent {
    private val orderUseCase by lazy { OrderUseCase.newInstance() }

    companion object {
        private var orderModel: OrderModel? = null
    }

    override fun getOrder(orderCode: String) {
        mView?.showLoading()
        subscribe(orderUseCase.getOrder(orderCode), {
            mView?.run {
                hideLoading()
                if (it.is_error) {
                    showErrorMessage(getErrorMessage(it.code))
                    return@subscribe
                }
                orderModel = it.data.toOrderModel()
                showOrderDetail(it.data.toOrderModel())
            }
        }, {
            it.printStackTrace()
            mView?.run {
                hideLoading()
                showErrorMessage(getErrorMessage(1001))
            }
        })
    }

    override fun destroyOrder(orderCode: String) {
        orderModel?.let { order ->
            mView?.showLoading()
            subscribe(orderUseCase.destroyOrder(orderCode, order.status), {
                mView?.run {
                    hideLoading()
                    if (it.is_error) {
                        showErrorMessage(getErrorMessage(it.code))
                        return@subscribe
                    }
                    getOrder(order.order_code)
                    RxBus.newInstance().onNext(Event(Constants.EventKey.UPDATE_STATUS_ORDER))
                    RxBus.newInstance().onNext(ValueEvent(Constants.EventKey.UPDATE_ORDER, order.apply { status = 4 }))

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

    override fun reOrder() {
        orderModel?.let { mView?.showReviewOrder(it) }
    }

    override fun onCompositedEventAdded() {
        super.onCompositedEventAdded()
        add(RxBus.newInstance().subscribe {
            when (it.key) {
                Constants.EventKey.RATING_SUCCESS -> orderModel?.let { order -> getOrder(order.order_code) }
            }
        })
    }

}