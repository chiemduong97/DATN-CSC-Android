package com.example.client.screens.profile.present

import com.example.client.app.Constants
import com.example.client.app.MyFirebaseService
import com.example.client.app.Preferences
import com.example.client.app.RxBus
import com.example.client.base.BasePresenterMVP
import com.example.client.screens.profile.fragment.IProfileView
import com.example.client.usecase.ProfileUseCase

class ProfilePresent(mView: IProfileView): BasePresenterMVP<IProfileView>(mView), IProfilePresent {
    private val profileUseCase by lazy { ProfileUseCase.newInstance() }
    private val preferences by lazy { Preferences.newInstance() }

    override fun bindData() {
        mView?.showUserInfo(preferences.profile)
    }

    override fun onLogout() {
        mView?.showLoading()
        val myFirebaseService = MyFirebaseService()
        myFirebaseService.deleteToken()
        subscribe(profileUseCase.updateDeviceToken(preferences.profile.email, ""),{
            mView?.run {
                hideLoading()
                if (it.is_error) {
                    showErrorMessage(getErrorMessage(it.code))
                    hideLoading()
                    return@subscribe
                }
                logout()
                preferences.deleteAll()
            }
        }, {
            it.printStackTrace()
            mView?.run {
                hideLoading()
                showErrorMessage(getErrorMessage(1001))
            }
        })
    }

    override fun navigateToContact() {
        mView?.toContactScreen(preferences.profile.id)
    }

    override fun onCompositedEventAdded() {
        super.onCompositedEventAdded()
        add(RxBus.newInstance().subscribe{
            when (it.key) {
                Constants.EventKey.UPDATE_PROFILE_AVATAR, Constants.EventKey.UPDATE_PROFILE_INFO -> bindData()
            }
        })
    }
}