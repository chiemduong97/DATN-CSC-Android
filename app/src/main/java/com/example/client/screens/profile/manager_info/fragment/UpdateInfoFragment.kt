package com.example.client.screens.profile.manager_info.fragment

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.example.client.R
import com.example.client.base.BaseFragmentMVP
import com.example.client.dialog.PrimaryDialog
import com.example.client.models.profile.ProfileModel
import com.example.client.screens.profile.manager_info.activity.IManagerProfileView
import com.example.client.screens.profile.manager_info.present.IManagerProfilePresent
import com.example.client.screens.profile.manager_info.present.ManagerProfilePresent
import com.example.client.screens.profile.navigate.NavigatorProfile
import kotlinx.android.synthetic.main.fragment_update_info.*
import java.text.SimpleDateFormat
import java.util.*

class UpdateInfoFragment : BaseFragmentMVP<IManagerProfilePresent>(), View.OnClickListener, IManagerProfileView {

    companion object {
        @JvmStatic
        fun newInstance() = UpdateInfoFragment()
    }

    override val presenter: IManagerProfilePresent
        get() = ManagerProfilePresent(this)

    private var errMessage = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_update_info, null)
    }


    override fun bindData() {
        presenter.bindData()
    }

    override fun bindEvent() {
        et_full_name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                errMessage = if (s.length < 6) {
                    requireContext().getString(R.string.register_name_invalid)
                } else {
                    ""
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        et_phone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val regex = "^(03|05|07|08|09|01[2|6|8|9])+([0-9]{8})\\b".toRegex()
                errMessage = if (et_phone.text.toString().matches(regex)) {
                    ""
                } else {
                    requireContext().getString(R.string.register_phone_invalid)
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        et_birthday.setOnClickListener(this)
        tv_update.setOnClickListener(this)
        imv_back.setOnClickListener(this)
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_update -> {
                if (errMessage.isNotEmpty()) {
                    showDialogErrorMessage(errMessage)
                    return
                }
                requireActivity().run {
                    val imn = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    currentFocus?.let {
                        imn.hideSoftInputFromWindow(it.windowToken, 0)
                    }
                }
                presenter.updateProfile(et_full_name.text.toString().trim(), et_birthday.text.toString().trim(), et_phone.text.toString().trim())
            }
            R.id.imv_back -> NavigatorProfile.popFragment()
            R.id.et_birthday -> {
                val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val calendar = Calendar.getInstance().apply {
                    if (et_birthday.text.toString().isNotBlank()) {
                        simpleDateFormat.parse(et_birthday.text.toString())?.let {
                            time = it
                        }
                    }
                }

                val datePickerDialog = DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                    calendar[year, month] = dayOfMonth
                    et_birthday.setText(simpleDateFormat.format(calendar.time))
                }, calendar[Calendar.YEAR], calendar[Calendar.MONTH], calendar[Calendar.DAY_OF_MONTH])
                datePickerDialog.show()
            }
        }
    }

    override fun showProfile(profileModel: ProfileModel) {
        profileModel.run {
            et_full_name.setText(fullname)
            et_birthday.setText(birthday)
            et_phone.setText(phone)
        }
    }

    override fun updateInfoSuccess() {
        PrimaryDialog({
            NavigatorProfile.popFragment()
        }, { })
                .setDescription(getString(R.string.update_info_update_profile_success))
                .show(childFragmentManager)
    }

    override fun updatePassSuccess() {}

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