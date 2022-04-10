package com.example.client.screens.branch.present

import com.example.client.R
import com.example.client.api.ApiClient
import com.example.client.api.service.BranchService
import com.example.client.app.Constants
import com.example.client.app.Preferences
import com.example.client.models.branch.BranchModel
import com.example.client.models.event.Event
import com.example.client.screens.branch.IBranchView
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BranchPresent (private var view: IBranchView?): IBranchPresent {
    override fun loadDataFromRes() {
        view?.showLoading()
        val service = ApiClient.getInstance().create(BranchService::class.java)
        service.getAll().enqueue(object : Callback<List<BranchModel>> {
            override fun onResponse(call: Call<List<BranchModel>>, response: Response<List<BranchModel>>) {
                response.body()?.let {
                    when {
                        it.isEmpty() -> {
                            view?.showEmptyData()
                        }
                        else -> {
                            Preferences.getInstance().branch?.let { branch ->
                                view?.showData(it, branch.id)
                            } ?: kotlin.run{
                                view?.showData(it, -1)
                            }
                        }
                    }
                    view?.hideLoading()
                } ?: kotlin.run {
                    view?.run {
                        showEmptyData()
                        hideLoading()
                    }
                }
            }

            override fun onFailure(call: Call<List<BranchModel>>, t: Throwable) {
                view?.run {
                    showEmptyData()
                    hideLoading()
                }
            }
        })
    }

    override fun saveBranch(branch: BranchModel) {
        Preferences.getInstance().branch = branch
//        RxBus.getInstance().onNext(Event(Constants.EventKey.CHANGE_BRANCH))
        EventBus.getDefault().post(Event(Constants.EventKey.CHANGE_BRANCH))
        loadDataFromRes()
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