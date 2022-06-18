package com.example.client.screens.profile.manager_info.present

import android.graphics.Bitmap
import com.example.client.base.IBasePresenter

interface IManagerProfilePresent: IBasePresenter {
    fun bindData()
    fun updateProfile(full_name: String, birthday: String, phone: String)
    fun updatePassword(oldPass :String, newPass: String)
    fun updateAvatar(avatar: Bitmap)
}