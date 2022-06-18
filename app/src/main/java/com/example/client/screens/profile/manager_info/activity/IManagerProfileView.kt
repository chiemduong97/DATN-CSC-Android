package com.example.client.screens.profile.manager_info.activity

import com.example.client.base.IBaseView
import com.example.client.models.profile.ProfileModel

interface IManagerProfileView: IBaseView {
    fun showProfile(profileModel: ProfileModel)
    fun updateInfoSuccess()
    fun updatePassSuccess()
    fun updateAvatarSuccess()
    fun showErrorMessage(errMessage: Int)
}