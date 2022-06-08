package com.example.client.screens.login.present

import com.example.client.app.Constants
import com.example.client.app.MyFirebaseService
import com.example.client.app.Preferences
import com.example.client.base.BasePresenter
import com.example.client.models.event.Event
import com.example.client.screens.login.activity.ILoginView
import com.example.client.usecase.ProfileUseCase
import org.greenrobot.eventbus.EventBus

class LoginPresent(mView: ILoginView) : BasePresenter<ILoginView>(mView),ILoginPresent {
    private val profileUseCase = ProfileUseCase.newInstance()
    override fun checkEmail(email: String) {
        mView?.showLoading()
        subscribe(profileUseCase.checkEmail(email),{
            mView?.run {

                if (it.isError) {
                    showErrorMessage(getErrorMessage(it.code))
                    hideLoading()
                    return@subscribe
                }
                next()
            }
        },{
            it.printStackTrace()
            mView?.run {
                showErrorMessage(getErrorMessage(1001))
                hideLoading()
            }
        })
    }

    override fun onLogin(email: String, password: String) {
        mView?.showLoading()
        val myFirebaseService = MyFirebaseService()
        add(myFirebaseService.token.subscribe {
            subscribe(profileUseCase.login(email, password), here@{
                mView?.run {
                    if (it.isError) {
                        hideLoading()
                        showErrorMessage(getErrorMessage(it.code))
                        return@here
                    }
                    Preferences.getInstance().accessToken = it.data.accessToken
                    setUserActive(email)
                }
            },{
                it.printStackTrace()
                mView?.run {
                    hideLoading()
                    showErrorMessage(getErrorMessage(1001))
                }
            })
        })
    }

    override fun setUserActive(email: String) {
        subscribe(profileUseCase.getUserByEmail(email),{
            mView?.run {
                if (it.isError) {
                    hideLoading()
                    showErrorMessage(getErrorMessage(it.code))
                    return@subscribe
                }
                Preferences.getInstance().profile = it.data.toProfileModel()
                val deviceToken = Preferences.getInstance().deviceToken
                onUpdateDeviceToken(email, deviceToken)
            }
        },{
            it.printStackTrace()
            mView?.run {
                hideLoading()
                showErrorMessage(getErrorMessage(1001))
            }
        })
    }

    override fun onUpdateDeviceToken(email: String, token: String) {
        subscribe(profileUseCase.updateDeviceToken(email, device_token = token),{
            mView?.run {
                hideLoading()
                if (it.isError) {
                    showErrorMessage(getErrorMessage(it.code))
                    return@subscribe
                }
                login()
                EventBus.getDefault().post(Event(Constants.EventKey.LOGIN_SUCCESS))
            }
        },{
            it.printStackTrace()
            mView?.run {
                hideLoading()
                showErrorMessage(getErrorMessage(1001))
            }
        })
    }

}