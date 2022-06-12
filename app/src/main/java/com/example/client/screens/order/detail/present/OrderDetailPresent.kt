package com.example.client.screens.order.detail.present

import com.example.client.R
import com.example.client.api.ApiClient
import com.example.client.api.service.BranchService
import com.example.client.api.service.OrderService
import com.example.client.app.Constants
import com.example.client.models.branch.BranchModel
import com.example.client.models.event.Event
import com.example.client.models.message.MessageModel
import com.example.client.models.order.OrderDetailModel
import com.example.client.models.order.OrderModel
import com.example.client.screens.order.detail.IOrderDetailView
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderDetailPresent(var view: IOrderDetailView?) : IOrderDetailPresent {
    override fun getOrderFromService(ordercode: String) {
        view?.showLoading()
        val service = ApiClient.newInstance().create(OrderService::class.java)
        service.getByOrderCode(ordercode).enqueue(object : Callback<OrderModel> {
            override fun onResponse(call: Call<OrderModel>, response: Response<OrderModel>) {
                response.body()?.let {
                    view?.initView(it)
                    view?.hideLoading()
                    getBranchFromService(it.branch_id)
                } ?: kotlin.run {
                    view?.run {
                        showErrorMessage(getErrorMessage(1001))
                        hideLoading()
                    }
                }
            }

            override fun onFailure(call: Call<OrderModel>, t: Throwable) {
                view?.run {
                    showErrorMessage(getErrorMessage(1001))
                    hideLoading()
                }
            }
        })

    }

    override fun getBranchFromService(id: Int) {
        view?.showLoading()
        val service = ApiClient.newInstance().create(BranchService::class.java)
        service.getById(id).enqueue(object: Callback<BranchModel> {
            override fun onResponse(call: Call<BranchModel>, response: Response<BranchModel>) {
                response.body()?.let {
                    view?.showBranchOrder(it)
                    view?.hideLoading()
                } ?: kotlin.run {
                    view?.run {
                        showErrorMessage(getErrorMessage(1001))
                        hideLoading()
                    }
                }
            }
            override fun onFailure(call: Call<BranchModel>, t: Throwable) {
                view?.run {
                    showErrorMessage(getErrorMessage(1001))
                    hideLoading()
                }
            }
        })
    }

    override fun getListOrderDetailFromService(ordercode: String) {
        view?.showLoading()
        val service = ApiClient.newInstance().create(OrderService::class.java)
        service.getListOrderDetail(ordercode).enqueue(object : Callback<List<OrderDetailModel>> {
            override fun onResponse(call: Call<List<OrderDetailModel>>, response: Response<List<OrderDetailModel>>) {
                response.body()?.let {
                    view?.showListProduct(it)
                    view?.hideLoading()
                } ?: kotlin.run {
                    view?.run {
                        showErrorMessage(getErrorMessage(1001))
                        hideLoading()
                    }
                }
            }

            override fun onFailure(call: Call<List<OrderDetailModel>>, t: Throwable) {
                view?.run {
                    showErrorMessage(getErrorMessage(1001))
                    hideLoading()
                }
            }
        })
    }

    override fun destroyOrder(ordercode: String, status: Int) {
        view?.showLoading()
        val service = ApiClient.newInstance().create(OrderService::class.java)
        service.destroy(ordercode, status).enqueue(object : Callback<MessageModel> {
            override fun onResponse(call: Call<MessageModel>, response: Response<MessageModel>) {
                response.body()?.let {
                    when {
                        it.isStatus -> {
                            view?.onRefresh()
                            EventBus.getDefault().post(Event(Constants.EventKey.UPDATE_STATUS_ORDER))
                        } else -> {
                            view?.showErrorMessage(getErrorMessage(it.code))
                        }
                    }
                    view?.hideLoading()
                } ?: kotlin.run {
                    view?.run {
                        showErrorMessage(getErrorMessage(1001))
                        hideLoading()
                    }
                }
            }

            override fun onFailure(call: Call<MessageModel>, t: Throwable) {
                view?.run {
                    showErrorMessage(getErrorMessage(1001))
                    hideLoading()
                }
            }
        })
    }

    private fun getErrorMessage(errCode: Int): Int {
        var errMessage = -1
        when (errCode) {
            Constants.ErrorCode.ERROR_1001 -> errMessage = R.string.err_code_1001
            Constants.ErrorCode.ERROR_1002 -> errMessage = R.string.err_code_1002
            Constants.ErrorCode.ERROR_1003 -> errMessage = R.string.err_code_1003
            Constants.ErrorCode.ERROR_1004 -> errMessage = R.string.err_code_1004
            Constants.ErrorCode.ERROR_1005 -> errMessage = R.string.err_code_1005
            Constants.ErrorCode.ERROR_1006 -> errMessage = R.string.err_code_1006
            Constants.ErrorCode.ERROR_1007 -> errMessage = R.string.err_code_1007
            Constants.ErrorCode.ERROR_1008 -> errMessage = R.string.err_code_1008
            Constants.ErrorCode.ERROR_1009 -> errMessage = R.string.err_code_1009
            Constants.ErrorCode.ERROR_1010 -> errMessage = R.string.err_code_1010
            Constants.ErrorCode.ERROR_1011 -> errMessage = R.string.err_code_1011
            Constants.ErrorCode.ERROR_1012 -> errMessage = R.string.err_code_1012
            Constants.ErrorCode.ERROR_1013 -> errMessage = R.string.err_code_1013
            Constants.ErrorCode.ERROR_1014 -> errMessage = R.string.err_code_1014
        }
        return errMessage
    }
}