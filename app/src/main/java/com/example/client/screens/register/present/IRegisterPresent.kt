package com.example.client.screens.register.present

import com.example.client.base.IBasePresenter

interface IRegisterPresent: IBasePresenter {
    fun register(fullname: String, phone: String, email: String, password: String)
    fun setUserActive(email: String)
    fun updateDeviceToken(email: String, token: String)
    fun sendEmail(email: String, phone: String)
    fun verification(email: String, code: String, fullname: String, phone: String, password: String)
}