package com.example.client.screens.register.present

import com.example.client.app.Constants
import com.example.client.base.BasePresenterMVP
import com.example.client.models.event.Event
import com.example.client.screens.register.activity.IRegisterView
import com.example.client.usecase.ProfileUseCase
import org.greenrobot.eventbus.EventBus

class RegisterPresent(mView: IRegisterView) : BasePresenterMVP<IRegisterView>(mView), IRegisterPresent {
    private val profileUseCase by lazy { ProfileUseCase.newInstance() }

    override fun register(fullname: String, phone: String, email: String, password: String) {
        subscribe(profileUseCase.resetFirebaseToken(), {
            subscribe(profileUseCase.register(fullname, phone, email, password), here@{
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
                register()
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

    override fun sendEmail(email: String, phone: String) {
        mView?.showLoading()
        subscribe(profileUseCase.sendEmail(email, phone, Constants.RequestType.REGISTER), {
            mView?.run {
                hideLoading()
                if (it.is_error) {
                    if (it.code == 1010) showVerificationDialog(true)
                    else showErrorMessage(getErrorMessage(it.code))
                    return@subscribe
                }
                showVerificationDialog(false)
            }
        }, {
            it.printStackTrace()
            mView?.run {
                hideLoading()
                showErrorMessage(getErrorMessage(1001))
            }
        })
    }

    override fun verification(email: String, code: String, fullname: String, phone: String, password: String) {
        mView?.showLoading()
        subscribe(profileUseCase.verification(email, code), {
            mView?.run {
                hideLoading()
                if (it.is_error) {
                    showErrorMessage(getErrorMessage(it.code))
                    return@subscribe
                }
                register(fullname, phone, email, password)
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