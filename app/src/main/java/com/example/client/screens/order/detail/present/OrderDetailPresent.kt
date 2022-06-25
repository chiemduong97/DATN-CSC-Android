package com.example.client.screens.order.detail.present

import com.example.client.base.BasePresenterMVP
import com.example.client.models.order.OrderModel
import com.example.client.models.order.toOrderDetails
import com.example.client.screens.order.detail.IOrderDetailView
import com.example.client.usecase.BranchUseCase
import com.example.client.usecase.OrderUseCase

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
                showBranch(it.data.toBranchModel())
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
        mView?.showLoading()
        subscribe(orderUseCase.destroyOrder(orderCode, orderModel!!.status), {
            mView?.run {
                hideLoading()
                if (it.is_error) {
                    showErrorMessage(getErrorMessage(it.code))
                    return@subscribe
                }
                onRefresh()
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