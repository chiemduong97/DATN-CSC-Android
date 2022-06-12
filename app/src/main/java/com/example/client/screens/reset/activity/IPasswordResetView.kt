package com.example.client.screens.reset.activity

import com.example.client.base.IBaseView

interface IPasswordResetView: IBaseView {
    fun verificationSuccess()
    fun resetPassSuccess()
    fun sendMailSuccess()
    fun showErrorMessage(errMessage: Int)
}