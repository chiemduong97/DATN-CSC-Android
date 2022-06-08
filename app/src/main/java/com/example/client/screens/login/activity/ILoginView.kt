package com.example.client.screens.login.activity

import com.example.client.base.IBaseView

interface ILoginView: IBaseView {
    fun next()
    fun login()
    fun showErrorMessage(errMessage: Int)
}