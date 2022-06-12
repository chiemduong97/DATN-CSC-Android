package com.example.client.screens.profile.present

import com.example.client.app.MyFirebaseService
import com.example.client.base.BasePresenterMVP
import com.example.client.screens.profile.fragment.IProfileView
import com.example.client.usecase.ProfileUseCase

class ProfilePresent(mView: IProfileView): BasePresenterMVP<IProfileView>(mView), IProfilePresent {
    private val profileUseCase by lazy { ProfileUseCase.newInstance() }
    private val profile = profileUseCase.getProfile()

    override fun bindData() {
        mView?.showUserInfo(profile)
    }

    override fun onLogout() {
        mView?.showLoading()
        val myFirebaseService = MyFirebaseService()
        myFirebaseService.deleteToken()
        subscribe(profileUseCase.updateDeviceToken(profile.email, ""),{
            mView?.run {
                hideLoading()
                if (it.is_error) {
                    showErrorMessage(getErrorMessage(it.code))
                    hideLoading()
                    return@subscribe
                }
                logout()
                profileUseCase.removeProfile()
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
        mView?.toContactScreen(profile.id)
    }
}