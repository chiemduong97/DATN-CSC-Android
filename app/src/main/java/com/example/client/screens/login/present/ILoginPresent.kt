package com.example.client.screens.login.present

import com.example.client.base.IBasePresenter

interface ILoginPresent: IBasePresenter {
    fun checkEmail(email: String)
    fun login(email: String, password: String)
    fun setUserActive(email: String)
}