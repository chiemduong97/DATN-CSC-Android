package com.example.client.screens.login.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.client.R
import com.example.client.app.Constants
import com.example.client.base.BaseActivityMVP
import com.example.client.models.event.Event
import com.example.client.screens.login.present.ILoginPresent
import com.example.client.screens.login.present.LoginPresent
import com.example.client.screens.register.activity.RegisterActivity
import kotlinx.android.synthetic.main.activity_login_email.*

class LoginEmailActivity : BaseActivityMVP<ILoginPresent>(), View.OnClickListener, ILoginView {

    companion object {
        @JvmStatic
        fun newInstance(from: Activity): Intent = Intent(from, LoginEmailActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_email)
    }

    override fun bindEvent() {
        et_email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isEmpty()) {
                    setButton(false, R.drawable.bg_btn_disable)
                } else {
                    val regex = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$".toRegex()
                    if (et_email.text.toString().matches(regex)) {
                        setButton(true, R.drawable.bg_btn)
                    } else {
                        setButton(false, R.drawable.bg_btn_disable)
                    }
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        tv_next.setOnClickListener(this)
        tv_register.setOnClickListener(this)
    }

    fun setButton(enable: Boolean, background: Int) {
        tv_next.isEnabled = enable
        tv_next.setBackgroundResource(background)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_next -> {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                currentFocus?.let {
                    imm.hideSoftInputFromWindow(it.windowToken, 0)
                    et_email.clearFocus()
                }
                presenter.checkEmail(et_email.text.toString())
            }
            R.id.tv_register -> startActivity(Intent(this@LoginEmailActivity, RegisterActivity::class.java))
        }
    }

    override fun next() {
        lnl_error.visibility = View.GONE
        startActivity(LoginPasswordActivity.newInstance(this, email = et_email.text.toString().trim()))
    }

    override fun loginSuccess() {}
    override fun showLoading() {
        rll_loading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        rll_loading.visibility = View.GONE
    }

    override fun onBackPress() {
        super.onBackPressed()
    }

    override fun showErrorMessage(errMessage: Int) {
        tv_error.text = getString(errMessage)
        lnl_error.visibility = View.VISIBLE
    }

    override val presenter: ILoginPresent
        get() = LoginPresent(this)

}