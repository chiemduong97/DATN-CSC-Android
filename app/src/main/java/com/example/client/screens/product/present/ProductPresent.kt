package com.example.client.screens.product.present

import com.example.client.R
import com.example.client.api.ApiClient
import com.example.client.api.service.ProductService
import com.example.client.app.Constants
import com.example.client.models.message.MessageModel
import com.example.client.models.product.ProductModel
import com.example.client.screens.product.activity.IProductView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProductPresent(private var view: IProductView?) : IProductPresent {

    override fun loadDataByCategory(category: Int) {
        view?.showLoading()
        val service = ApiClient.getInstance().create(ProductService::class.java)
        service.getByCategory(category).enqueue(object : Callback<List<ProductModel>> {
            override fun onResponse(call: Call<List<ProductModel>>, response: Response<List<ProductModel>?>) {
                response.body()?.let {
                    when {
                        it.isEmpty() -> {
                            view?.showEmptyData()
                        }
                        else -> {
                            view?.showData(it)
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

            override fun onFailure(call: Call<List<ProductModel>>, t: Throwable) {
                view?.run {
                    showErrorMessage(1001)
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
        }
        return errMessage
    }


}