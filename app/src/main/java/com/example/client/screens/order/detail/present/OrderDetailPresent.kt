package com.example.client.screens.order.detail.present

import com.example.client.app.Constants
import com.example.client.app.Preferences
import com.example.client.app.RxBus
import com.example.client.base.BasePresenterMVP
import com.example.client.models.event.Event
import com.example.client.models.event.ValueEvent
import com.example.client.models.order.OrderModel
import com.example.client.models.order.toOrderDetails
import com.example.client.screens.order.detail.IOrderDetailView
import com.example.client.usecase.BranchUseCase
import com.example.client.usecase.OrderUseCase
import com.google.android.gms.maps.model.LatLng

class OrderDetailPresent(mView: IOrderDetailView) : BasePresenterMVP<IOrderDetailView>(mView), IOrderDetailPresent {
    private val orderUseCase by lazy { OrderUseCase.newInstance() }
    private val branchUseCase by lazy { BranchUseCase.newInstance() }

    companion object {
        private var orderModel: OrderModel? = null
    }

    override fun getOrder(orderCode: String) {
        mView?.showLoading()
        subscribe(orderUseCase.getOrder(orderCode), {
            mView?.run {
                if (it.is_error) {
                    hideLoading()
                    showErrorMessage(getErrorMessage(it.code))
                    return@subscribe
                }
                orderModel = it.data.toOrderModel()
                showOrderDetail(it.data.toOrderModel())
                getBranch(orderModel!!.branch_id)
            }
        }, {
            it.printStackTrace()
            mView?.run {
                hideLoading()
                showErrorMessage(getErrorMessage(1001))
            }
        })
    }

    private fun getBranch(id: Int) {
        subscribe(branchUseCase.getBranch(id), {
            mView?.run {
                hideLoading()
                if (it.is_error) {
                    showErrorMessage(getErrorMessage(it.code))
                    return@subscribe
                }
                showBranch(it.data.toBranchModel(LatLng(Preferences.newInstance().orderLocation.lat, Preferences.newInstance().orderLocation.lng)))
            }
        }, {
            it.printStackTrace()
            mView?.run {
                hideLoading()
                showErrorMessage(getErrorMessage(1001))
            }
        })
    }

    override fun getOrderDetails(orderCode: String) {
        mView?.showLoading()
        subscribe(orderUseCase.getOrderDetails(orderCode), {
            mView?.run {
                hideLoading()
                if (it.is_error) {
                    showErrorMessage(getErrorMessage(it.code))
                    return@subscribe
                }
                showProducts(it.data.toOrderDetails())
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

}