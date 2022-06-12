package com.example.client.screens.login.present

import com.example.client.app.Constants
import com.example.client.app.Preferences
import com.example.client.base.BasePresenterMVP
import com.example.client.models.event.Event
import com.example.client.screens.login.activity.ILoginView
import com.example.client.usecase.ProfileUseCase
import org.greenrobot.eventbus.EventBus

class LoginPresent(mView: ILoginView) : BasePresenterMVP<ILoginView>(mView), ILoginPresent {
    private val profileUseCase by lazy { ProfileUseCase.newInstance() }
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
        subscribe(profileUseCase.resetFirebaseToken(), {
            subscribe(profileUseCase.login(email, password), here@{
                mView?.run {
                    if (it.is_error) {
                        hideLoading()
                        showErrorMessage(getErrorMessage(it.code))
                        return@here
                    }
                    profileUseCase.setAccessToken(it.data.access_token)
                    setUserActive(email)
                }
            }, {
                it.printStackTrace()
                mView?.run {
                    hideLoading()
                    showErrorMessage(getErrorMessage(1001))
                }
            })
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
                profileUseCase.setProfile(it.data.toProfileModel())
                updateDeviceToken(email, profileUseCase.getDeviceToken())
            }
        }, {
            it.printStackTrace()
            mView?.run {
                hideLoading()
                showErrorMessage(getErrorMessage(1001))
            }
        })
    }

    override fun updateDeviceToken(email: String, token: String) {
        subscribe(profileUseCase.updateDeviceToken(email, device_token = token), {
            mView?.run {
                hideLoading()
                if (it.is_error) {
                    showErrorMessage(getErrorMessage(it.code))
                    return@subscribe
                }
                login()
                EventBus.getDefault().post(Event(Constants.EventKey.LOGIN_SUCCESS))
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