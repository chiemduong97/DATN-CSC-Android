package com.example.client.app

import android.app.Activity
import android.content.Intent
import com.example.client.utils.MomoCallBack
import vn.momo.momo_partner.AppMoMoLib
import vn.momo.momo_partner.MoMoParameterNamePayment
import java.util.HashMap

class Momo {
    companion object {
        private var mInstance: Momo? = null
        fun newInstance(): Momo {
            if (mInstance == null) {
                mInstance = Momo()
            }
            return mInstance!!
        }
    }

    fun requestMoMo(activity: Activity, amount: Int) {
        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT)
        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT)
        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN)
        val eventValue: MutableMap<String, Any> = HashMap()
        eventValue[MoMoParameterNamePayment.MERCHANT_NAME] = Constants.MoMoConstants.MERCHANT_NAME
        eventValue[MoMoParameterNamePayment.MERCHANT_CODE] = Constants.MoMoConstants.MERCHANT_CODE
        eventValue[MoMoParameterNamePayment.AMOUNT] = amount
        eventValue[MoMoParameterNamePayment.DESCRIPTION] = Constants.MoMoConstants.DESCRIPTION
//        eventValue["orderId"] = "CSCMomoPay" + System.currentTimeMillis()
//        eventValue["orderLabel"] = "Mã đơn hàng"
        eventValue[MoMoParameterNamePayment.FEE] = 0
        eventValue[MoMoParameterNamePayment.MERCHANT_NAME_LABEL] = Constants.MoMoConstants.MERCHANT_NAME_LABEL
        AppMoMoLib.getInstance().requestMoMoCallBack(activity, eventValue)
    }

    fun callbackResult(requestCode: Int, data: Intent?, callback: MomoCallBack) {
        if (requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO) {
            data?.let {
                if (it.getIntExtra("status", -1) == 0 && !it.getStringExtra("data").isNullOrEmpty()) {
                    callback.onSuccess(it.getStringExtra("data")!!, it.getStringExtra("phonenumber")!!)
                } else {
                    callback.onError()
                }
            } ?: kotlin.run {
                callback.onError()
            }
        } else {
            callback.onError()
        }

    }
}