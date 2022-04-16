package com.example.client.screens.map.present

import com.example.client.R
import com.example.client.api.ApiClient
import com.example.client.api.service.UserService
import com.example.client.app.Constants
import com.example.client.app.Preferences
import com.example.client.models.event.Event
import com.example.client.models.message.MessageModel
import com.example.client.screens.map.activity.IMapsView
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsPresent(var view: IMapsView): IMapsPresent {
    override fun updateLocation(latitude: Double, longitude: Double, address: String) {
        view.showLoading()
        val service = ApiClient.getInstance().create(UserService::class.java)
        val profile = Preferences.getInstance().profile.apply {
            this.latitude = latitude
            this.longitude = longitude
            this.address = address
        }
        service.updateLocation(profile.email, latitude, longitude, address).enqueue(object: Callback<MessageModel> {
            override fun onResponse(call: Call<MessageModel>, response: Response<MessageModel>) {
                response.body()?.let {
                    when {
                        it.isStatus -> {
                            Preferences.getInstance().profile = profile
                            EventBus.getDefault().post(Event(Constants.EventKey.UPDATE_LOCATION))
                            view.showSuccess()
                        }
                        else -> {
                            view.showErrorMessage(getErrorMessage(it.code))
                        }
                    }
                    view.hideLoading()
                } ?: kotlin.run {
                    view.run {
                        showErrorMessage(getErrorMessage(1001))
                        hideLoading()
                    }
                }
            }
            override fun onFailure(call: Call<MessageModel>, t: Throwable) {
                view.run {
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