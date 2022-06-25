package com.example.client.screens.register.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.CompoundButton
import com.example.client.R
import com.example.client.base.BaseActivityMVP
import com.example.client.dialog.PrimaryDialog
import com.example.client.dialog.VerificationDialog
import com.example.client.screens.main.activity.MainActivity
import com.example.client.screens.register.present.IRegisterPresent
import com.example.client.screens.register.present.RegisterPresent
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivityMVP<IRegisterPresent>(), View.OnClickListener, IRegisterView {

    override val presenter: IRegisterPresent
        get() = RegisterPresent(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    override fun bindComponent() {
        et_password.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        et_confirm.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
    }

    override fun bindEvent() {
        tbt_eye_password.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
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
        tbt_eye_confirm.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            et_confirm.run {
                if (isChecked) {
                    inputType = InputType.TYPE_CLASS_TEXT
                    setSelection(text.length)
                } else {
                    inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    setSelection(text.length)
                }
            }
        }
        et_full_name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                tv_full_name_error.run {
                    if (s.length < 6) {
                        text = context.getString(R.string.register_name_invalid)
                        visibility = View.VISIBLE
                    } else {
                        text = ""
                        visibility = View.GONE
                    }
                }
                checkButton()
            }

            override fun afterTextChanged(s: Editable) {}
        })
        et_phone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val regex = "^(03|05|07|08|09|01[2|6|8|9])+([0-9]{8})\\b".toRegex()
                tv_phone_error.run {
                    if (et_phone.text.toString().matches(regex)) {
                        text = ""
                        visibility = View.GONE
                    } else {
                        text = context.getString(R.string.register_phone_invalid)
                        visibility = View.VISIBLE
                    }
                }
                checkButton()
            }

            override fun afterTextChanged(s: Editable) {}
        })
        et_email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                tv_email_error.run {
                    if (s.length < 6) {
                        text = context.getString(R.string.register_email_length_invalid)
                        visibility = View.VISIBLE
                    } else {
                        val regex = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$".toRegex()
                        if (et_email.text.toString().matches(regex)) {
                            text = ""
                            visibility = View.GONE
                        } else {
                            text = context.getString(R.string.register_email_invalid)
                            visibility = View.VISIBLE
                        }
                    }
                }
                checkButton()
            }

            override fun afterTextChanged(s: Editable) {}
        })
        et_password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                tv_password_error.run {
                    if (s.length < 6) {
                        text = context.getString(R.string.register_password_length_invalid)
                        visibility = View.VISIBLE
                    } else {
                        text = ""
                        visibility = View.GONE
                    }
                }
                checkButton()
            }

            override fun afterTextChanged(s: Editable) {}
        })
        et_confirm.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                tv_confirm_error.run {
                    if (et_password.text.toString() != et_confirm.text.toString()) {
                        setText(R.string.register_confirm_password_invalid)
                        visibility = View.VISIBLE
                        scroll_view.post { scroll_view.scrollTo(0, scroll_view.height) }
                    } else {
                        text = ""
                        visibility = View.GONE
                    }
                }
                checkButton()
            }

            override fun afterTextChanged(s: Editable) {}
        })
        tv_register.setOnClickListener(this)
        imv_back.setOnClickListener(this)
    }

    private fun checkButton() {
        if ((tv_full_name_error.text.toString().isEmpty() &&
                        tv_phone_error.text.toString().isEmpty() &&
                        tv_email_error.text.toString().isEmpty() &&
                        tv_password_error.text.toString().isEmpty() &&
                        tv_confirm_error.text.toString().isEmpty()) ||
                (et_full_name.text.toString().isNotEmpty() &&
                        et_phone.text.toString().isNotEmpty() &&
                        et_email.text.toString().isNotEmpty() &&
                        et_password.text.toString().isNotEmpty() &&
                        et_confirm.text.toString().isNotEmpty())) {
            setButton(true, R.drawable.bg_btn)
        } else {
            setButton(false, R.drawable.bg_btn_disable)
        }
    }

    private fun setButton(enable: Boolean, background: Int) {
        tv_register.isEnabled = enable
        tv_register.setBackgroundResource(background)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.imv_back -> {
                finish()
                onBackPressed()
            }
            R.id.tv_register -> {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                currentFocus?.let {
                    imm.hideSoftInputFromWindow(it.windowToken, 0)
                }
                presenter.sendEmail(et_email.text.toString(), et_phone.text.toString())
            }
        }
    }

    override fun register() {
        PrimaryDialog({
            finish()
            startActivity(Intent(this, MainActivity::class.java))
        }, {})
                .setDescription(getString(R.string.register_success))
                .show(supportFragmentManager)
    }

    override fun showVerificationDialog(isError: Boolean) {
        VerificationDialog({
            VerificationDialog.verificationCode?.let {
                presenter.verification(et_email.text.toString(), it,
                        et_full_name.text.toString(),
                        et_phone.text.toString(),
                        et_password.text.toString())
            }
        }, {})
                .setDescription(if (isError) getString(R.string.err_code_1010) else getString(R.string.dialog_verification_description))
                .show(supportFragmentManager)
    }

    override fun showLoading() {
        rll_loading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        rll_loading.visibility = View.GONE
    }

    override fun onBackPress() {}

    override fun showErrorMessage(errMessage: Int) {
        showDialogErrorMessage(getString(errMessage))
    }

}