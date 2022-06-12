package com.example.client.screens.register.activity

import com.example.client.base.IBaseView

interface IRegisterView: IBaseView {
    fun register()
    fun showVerificationDialog(isError: Boolean)
    fun showErrorMessage(errMessage: Int)
}