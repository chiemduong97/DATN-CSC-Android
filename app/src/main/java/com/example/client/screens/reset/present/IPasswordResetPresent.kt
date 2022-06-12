package com.example.client.screens.reset.present

import android.widget.TextView
import com.example.client.base.IBasePresenter

interface IPasswordResetPresent: IBasePresenter {
    fun sendEmail(email: String)
    fun verification(email: String, code: String)
    fun resetPass(email: String, password: String)
    fun countDownTimer(tv: TextView, time: Int)
}