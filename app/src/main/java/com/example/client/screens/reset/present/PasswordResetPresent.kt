package com.example.client.screens.reset.present

import android.graphics.Color
import android.widget.TextView
import com.example.client.R
import com.example.client.app.Constants
import com.example.client.app.RxBus
import com.example.client.base.BasePresenterMVP
import com.example.client.models.event.Event
import com.example.client.screens.reset.activity.IPasswordResetView
import com.example.client.usecase.ProfileUseCase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.text.MessageFormat
import java.util.*
import java.util.concurrent.TimeUnit

class PasswordResetPresent(mView: IPasswordResetView) : BasePresenterMVP<IPasswordResetView>(mView), IPasswordResetPresent {
    private val profileUseCase by lazy { ProfileUseCase.newInstance() }

    override fun sendEmail(email: String) {
        mView?.showLoading()
        subscribe(profileUseCase.sendEmail(email, "000", Constants.RequestType.RESET_PASSWORD), {
            mView?.run {
                hideLoading()
                if (it.is_error) {
                    if (it.code == 1010) sendMailSuccess()
                    showErrorMessage(getErrorMessage(it.code))
                    return@subscribe
                }
                sendMailSuccess()
            }
        }, {
            it.printStackTrace()
            mView?.run {
                hideLoading()
                showErrorMessage(getErrorMessage(1001))
            }
        })
    }

    override fun verification(email: String, code: String) {
        mView?.showLoading()
        subscribe(profileUseCase.verification(email, code), {
            mView?.run {
                hideLoading()
                if (it.is_error) {
                    showErrorMessage(getErrorMessage(it.code))
                    return@subscribe
                }
                verificationSuccess()
            }
        }, {
            it.printStackTrace()
            mView?.run {
                hideLoading()
                showErrorMessage(getErrorMessage(1001))
            }
        })
    }

    override fun resetPass(email: String, password: String) {
        mView?.showLoading()
        subscribe(profileUseCase.resetPass(email, password), {
            mView?.run {
                hideLoading()
                if (it.is_error) {
                    showErrorMessage(getErrorMessage(it.code))
                    return@subscribe
                }
                resetPassSuccess()
                RxBus.newInstance().onNext(Event(Constants.EventKey.RESET_SUCCESS))
            }
        }, {
            it.printStackTrace()
            mView?.run {
                hideLoading()
                showErrorMessage(getErrorMessage(1001))
            }
        })
    }

    override fun countDownTimer(tv: TextView, time: Int) {
        add(Observable.interval(1, TimeUnit.SECONDS)
                .take(time.toLong())
                .map { v: Long -> v+1 }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val value = time - it
                    val minutes = (value % 3600 / 60).toInt()
                    val seconds = (value % 60).toInt()
                    val timeString = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
                    tv.text = MessageFormat.format("Gửi mail xác thực ({0})", timeString)
                    tv.setTextColor(Color.GRAY)
                    tv.isEnabled = false
                },{
                    it.printStackTrace()
                    tv.setText(R.string.reset_text_send_email)
                    tv.setTextColor(Color.BLUE)
                    tv.isEnabled = true
                }))
    }

}