package com.example.client.screens.login.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.example.client.R
import com.example.client.app.Constants
import com.example.client.base.BaseActivityMVP
import com.example.client.models.event.Event
import com.example.client.screens.login.present.LoginPresent
import com.example.client.screens.main.activity.MainActivity
import com.example.client.screens.reset.activity.PasswordResetActivity
import kotlinx.android.synthetic.main.activity_login_password.*

class LoginPasswordActivity : BaseActivityMVP<LoginPresent>(), View.OnClickListener, ILoginView {
    companion object {
        @JvmStatic
        fun newInstance(from: Activity, email: String): Intent = Intent(from, LoginPasswordActivity::class.java).apply {
            putExtra(Constants.EMAIL, email)
        }
    }

    private val email by lazy { intent?.getStringExtra(Constants.EMAIL) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_password)
    }

    fun setButton(enable: Boolean, background: Int) {
        tv_login.run {
            isEnabled = enable
            setBackgroundResource(background)
        }
    }

    override fun bindEvent() {
        et_password.run {
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            tbt_eye.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
                if (isChecked) {
                    inputType = InputType.TYPE_CLASS_TEXT
                    setSelection(et_password.text.length)
                } else {
                    inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    setSelection(et_password.text.length)
                }
            }
            addTextChangedListener(object : TextWatcher {
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
        }

        tv_login.setOnClickListener(this)
        imv_back.setOnClickListener(this)
        tv_reset.setOnClickListener(this)
    }

    override fun bindEventBus(event: Event) {
        if (event.key == Constants.EventKey.RESET_SUCCESS) finish()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_login -> {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                currentFocus?.let {
                    imm.hideSoftInputFromWindow(it.windowToken, 0)
                    et_password.clearFocus()
                }
                email?.let { presenter.login(it, et_password.text.toString().trim()) }
            }
            R.id.imv_back -> {
                finish()
                onBackPressed()
            }
            R.id.tv_reset -> {
                email?.let {
                    startActivity(PasswordResetActivity.newInstance(this, it))
                }
            }
        }
    }

    override operator fun next() {}
    override fun login() {
        lnl_error.visibility = View.GONE
        finish()
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun showLoading() {
        rll_loading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        rll_loading.visibility = View.GONE
    }

    override fun onBackPress() {}

    override fun showErrorMessage(errMessage: Int) {
        tv_error.text = getString(errMessage)
        lnl_error.visibility = View.VISIBLE
    }

    override val presenter: LoginPresent
        get() = LoginPresent(this)
}