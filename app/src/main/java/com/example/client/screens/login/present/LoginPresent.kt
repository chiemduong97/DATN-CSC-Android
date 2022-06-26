package com.example.client.screens.login.present

import com.example.client.app.Constants
import com.example.client.app.Preferences
import com.example.client.app.RxBus
import com.example.client.base.BasePresenterMVP
import com.example.client.models.event.Event
import com.example.client.screens.login.activity.ILoginView
import com.example.client.usecase.ProfileUseCase

class LoginPresent(mView: ILoginView) : BasePresenterMVP<ILoginView>(mView), ILoginPresent {
    private val profileUseCase by lazy { ProfileUseCase.newInstance() }
    private val preferences by lazy { Preferences.newInstance() }
    override fun checkEmail(email: String) {
        mView?.showLoading()
        subscribe(profileUseCase.checkEmail(email), {
            mView?.run {
                hideLoading()
                if (it.is_error) {
                    showErrorMessage(getErrorMessage(it.code))
                    return@subscribe
                }
                next()
            }
        }, {
            it.printStackTrace()
            mView?.run {
                showErrorMessage(getErrorMessage(1001))
                hideLoading()
            }
        })
    }

    override fun login(email: String, password: String) {
        mView?.showLoading()
        profileUseCase.resetFirebaseToken(email)
        subscribe(profileUseCase.login(email, password), here@{
            mView?.run {
                if (it.is_error) {
                    hideLoading()
                    showErrorMessage(getErrorMessage(it.code))
                    return@here
                }
                preferences.accessToken = it.data.access_token
                setUserActive(email)
            }
        }, {
            it.printStackTrace()
            mView?.run {
                hideLoading()
                showErrorMessage(getErrorMessage(1001))
            }
        })
    }

    override fun setUserActive(email: String) {
        subscribe(profileUseCase.getUserByEmail(email), {
            mView?.run {
                if (it.is_error) {
                    hideLoading()
                    showErrorMessage(getErrorMessage(it.code))
                    return@subscribe
                }
                preferences.profile = it.data.toProfileModel()
                login()
                RxBus.newInstance().onNext(Event(Constants.EventKey.LOGIN_SUCCESS))
            }
        }, {
            it.printStackTrace()
            mView?.run {
                hideLoading()
                showErrorMessage(getErrorMessage(1001))
            }
        })
    }

    override fun onCompositedEventAdded() {
        super.onCompositedEventAdded()
        add(RxBus.newInstance().subscribe {
            when (it.key) {
                Constants.EventKey.LOGIN_SUCCESS, Constants.EventKey.RESET_SUCCESS -> mView?.onBackPress()
            }
        })
    }

}