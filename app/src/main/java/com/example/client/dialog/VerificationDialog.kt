package com.example.client.dialog

import `in`.aabhasjindal.otptextview.OTPListener
import `in`.aabhasjindal.otptextview.OtpTextView
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import com.example.client.R
import com.example.client.base.BaseDialog

class VerificationDialog(private val onOk: () -> Unit, private val onCancel: () -> Unit) : BaseDialog(), View.OnClickListener {

    private var tvOk: TextView? = null
    private var tvCancel: TextView? = null
    private var otpCode: OtpTextView? = null
    private var tvDescription: TextView? = null
    private var description: String? = null

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.dialog_verification, null)
        tvOk = rootView.findViewById(R.id.tv_ok)
        tvCancel = rootView.findViewById(R.id.tv_cancel)
        otpCode = rootView.findViewById(R.id.otp_code)
        tvDescription = rootView.findViewById(R.id.tv_description)
        return rootView
    }

    override fun bindEvent() {
        tvOk?.setOnClickListener(this)
        tvCancel?.setOnClickListener(this)
        otpCode?.otpListener = (object : OTPListener {
            override fun onInteractionListener() {}
            override fun onOTPComplete(otp: String) {
                enableOK()
                verificationCode = otp
            }
        })
    }

    override fun bindComponent() {
        tvDescription?.text = description
        disableOk()
    }

    override fun onClick(v: View) {
        dialog?.run {
            when (v.id) {
                R.id.tv_ok -> {
                    onOk.invoke()
                    dismiss()
                    otpCode?.setOTP("")
                }
                R.id.tv_cancel -> {
                    onCancel.invoke()
                    dismiss()
                    otpCode?.setOTP("")
                }
                else -> {
                }
            }
        }
    }

    fun show(fragmentManager: FragmentManager?) {
        fragmentManager ?: return
        show(fragmentManager, tag)
    }

    private fun disableOk() {
        tvOk?.run {
            isEnabled = false
            setBackgroundResource(R.drawable.bg_btn_disable)
        }
    }

    private fun enableOK() {
        tvOk?.run {
            isEnabled = true
            setBackgroundResource(R.drawable.bg_btn)
        }
    }


    fun setDescription(description: String): VerificationDialog {
        this.description = description
        return this
    }

    companion object {
        @JvmField
        var verificationCode: String? = null
    }


}