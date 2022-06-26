package com.example.client.screens.login.activity

import com.example.client.base.IBaseView

interface ILoginView: IBaseView {
    fun next()
    fun loginSuccess()
    fun showErrorMessage(errMessage: Int)
}