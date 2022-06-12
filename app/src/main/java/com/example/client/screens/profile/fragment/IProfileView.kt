package com.example.client.screens.profile.fragment

import com.example.client.base.IBaseView
import com.example.client.models.profile.ProfileModel

interface IProfileView: IBaseView {
    fun logout()
    fun showUserInfo(profile: ProfileModel)
    fun showErrorMessage(errMessage: Int)
    fun toContactScreen(id: Int)
}