package com.example.client.screens.profile.manager_info.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.example.client.R
import com.example.client.base.BaseFragmentMVP
import com.example.client.dialog.PrimaryDialog
import com.example.client.models.profile.ProfileModel
import com.example.client.screens.profile.manager_info.activity.IManagerProfileView
import com.example.client.screens.profile.manager_info.present.IManagerProfilePresent
import com.example.client.screens.profile.manager_info.present.ManagerProfilePresent
import com.example.client.screens.profile.navigate.NavigatorProfile
import kotlinx.android.synthetic.main.fragment_update_password.*

class UpdatePasswordFragment : BaseFragmentMVP<IManagerProfilePresent>(), View.OnClickListener, IManagerProfileView {

    companion object {
        @JvmStatic
        fun newInstance() = UpdatePasswordFragment()
    }

    override val presenter: IManagerProfilePresent
        get() = ManagerProfilePresent(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_update_password, null)
    }

    override fun bindComponent() {
        et_old_password.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        et_new_password.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        et_confirm.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
    }

    override fun bindEvent() {

        tbt_eye_old_password.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            et_old_password.run {
                if (isChecked) {
                    inputType = InputType.TYPE_CLASS_TEXT
                    setSelection(text.length)
                } else {
                    inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    setSelection(text.length)
                }
            }
        }
        tbt_eye_new_password.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            et_new_password.run {
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
        et_old_password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                tv_old_password_error.run {
                    if (s.length < 6) {
                        text = getString(R.string.register_password_length_invalid)
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
        et_new_password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                tv_new_password_error.run {
                    if (s.length < 6) {
                        text = getString(R.string.register_password_length_invalid)
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
                tv_error_confirm.run {
                    if (et_new_password.text.toString() != et_confirm.text.toString()) {
                        text = getString(R.string.register_confirm_password_invalid)
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
        imv_back.setOnClickListener(this)
        tv_update.setOnClickListener(this)
    }

    fun checkButton() {
        if (tv_old_password_error.text.toString().isEmpty() && tv_new_password_error.text.toString().isEmpty() && tv_error_confirm.text.toString().isEmpty()) {
            setButton(true, R.drawable.bg_btn)
        } else {
            setButton(false, R.drawable.bg_btn_disable)
        }
    }

    private fun setButton(enable: Boolean, background: Int) {
        tv_update.run {
            isEnabled = enable
            setBackgroundResource(background)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_update -> {
                requireActivity().run {
                    val imn = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    requireActivity().currentFocus?.let {
                        imn.hideSoftInputFromWindow(it.windowToken, 0)
                    }
                }
                presenter.updatePassword(et_old_password.text.toString().trim(), et_new_password.text.toString().trim())
            }
            R.id.imv_back -> NavigatorProfile.popFragment()
        }
    }

    override fun showProfile(profileModel: ProfileModel) {}
    override fun updateInfoSuccess() {}
    override fun updatePassSuccess() {
        PrimaryDialog({
            NavigatorProfile.popFragment()
        }, { })
                .setDescription(getString(R.string.update_info_change_password_success))
                .show(childFragmentManager)
    }

    override fun updateAvatarSuccess() {}
    override fun showLoading() {
        rll_loading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        rll_loading.visibility = View.GONE
    }

    override fun onBackPress() {
        NavigatorProfile.popFragment()
    }

    override fun showErrorMessage(errMessage: Int) {
        showDialogErrorMessage(getString(errMessage))
    }
}