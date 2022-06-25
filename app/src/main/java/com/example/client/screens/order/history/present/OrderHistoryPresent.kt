package com.example.client.screens.order.history.present

import com.example.client.R
import com.example.client.api.ApiClient
import com.example.client.api.service.OrderService
import com.example.client.app.Constants
import com.example.client.app.Preferences
import com.example.client.models.order.OrderModel
import com.example.client.screens.order.history.activity.IOrderHistoryView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderHistoryPresent(var view: IOrderHistoryView?) : IOrderHistoryPresent {
    override fun getListOrderFromService() {
        view?.showLoading()
        val service = ApiClient.newInstance().create(OrderService::class.java)
        val profile = Preferences.newInstance().profile
//        service.getOrdersByUser(profile.id).enqueue(object : Callback<List<OrderModel>>{
//            override fun onResponse(call: Call<List<OrderModel>>, response: Response<List<OrderModel>>) {
//                response.body()?.let {
//                    when {
//                        it.isNotEmpty() -> {
//                            view?.showData(it)
//                        }
//                        else -> {
//                            view?.showEmpty()
//                        }
//                    }
//                    view?.hideLoading()
//                } ?: kotlin.run {
//                    view?.run {
//                        showErrorMessage(getErrorMessage(1001))
//                        hideLoading()
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<List<OrderModel>>, t: Throwable) {
//                view?.run {
//                    showErrorMessage(getErrorMessage(1001))
//                    hideLoading()
//                }
//            }
//        })
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