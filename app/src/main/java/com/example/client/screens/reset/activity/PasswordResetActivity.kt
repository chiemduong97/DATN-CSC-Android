package com.example.client.screens.reset.activity

import `in`.aabhasjindal.otptextview.OTPListener
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.CompoundButton
import com.example.client.R
import com.example.client.app.Constants
import com.example.client.base.BaseActivityMVP
import com.example.client.dialog.PrimaryDialog
import com.example.client.screens.reset.present.IPasswordResetPresent
import com.example.client.screens.reset.present.PasswordResetPresent
import kotlinx.android.synthetic.main.activity_password_reset.*

class PasswordResetActivity : BaseActivityMVP<IPasswordResetPresent>(), View.OnClickListener, IPasswordResetView {

    companion object {
        @JvmStatic
        fun newInstance(from: Activity, email: String): Intent = Intent(from, PasswordResetActivity::class.java).apply {
            putExtra(Constants.EMAIL, email)
        }
    }

    private val email by lazy { intent?.getStringExtra(Constants.EMAIL) }

    override val presenter: IPasswordResetPresent
        get() = PasswordResetPresent(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_reset)
    }

    override fun bindComponent() {
        et_password.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
    }

    override fun bindEvent() {
        tbt_eye.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            et_password.run {
                if (isChecked) {
                    inputType = InputType.TYPE_CLASS_TEXT
                    setSelection(text.length)
                } else {
                    inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    setSelection(text.length)
                }
            }
        }
        et_password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length < 6) {
                    setButton(false, R.drawable.bg_btn_disable)
                } else {
                    setButton(true, R.drawable.bg_btn)
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        tv_send_email.setOnClickListener(this)
        imv_back.setOnClickListener(this)
        tv_reset.setOnClickListener(this)
        otp_code.otpListener = object : OTPListener {
            override fun onInteractionListener() {}
            override fun onOTPComplete(otp: String) {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                currentFocus?.let {
                    imm.hideSoftInputFromWindow(it.windowToken, 0)
                }
                email?.let {
                    presenter.verification(it, otp)
                }
            }
        }
    }

    fun setButton(enable: Boolean, background: Int) {
        tv_reset.run {
            isEnabled = enable
            setBackgroundResource(background)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_send_email -> {
                email?.let {
                    presenter.sendEmail(it)
                }
            }
            R.id.imv_back -> {
                finish()
                onBackPressed()
            }
            R.id.tv_reset -> {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                currentFocus?.let {
                    imm.hideSoftInputFromWindow(it.windowToken, 0)
                }
                email?.let {
                    presenter.resetPass(it, et_password.text.toString())
                }
            }
        }
    }

    override fun verificationSuccess() {
        otp_code.showSuccess()
        otp_code.clearFocus()
        view_password.visibility = View.VISIBLE
    }

    override fun resetPassSuccess() {
        PrimaryDialog({ finish() }, { })
                .setDescription(getString(R.string.reset_password_success))
                .hideBtnCancel()
                .show(supportFragmentManager)
    }

    override fun sendMailSuccess() {
        tv_send_email_error.visibility = View.GONE
        lnl_code.visibility = View.VISIBLE
        otp_code.otp = ""
        presenter.countDownTimer(tv_send_email, 300)
    }


    override fun showErrorMessage(errMessage: Int) {
        showDialogErrorMessage(getString(errMessage))
        otp_code.otp = ""
    }

    override fun showLoading() {
        rll_loading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        rll_loading.visibility = View.GONE
    }

    override fun onBackPress() {}


}