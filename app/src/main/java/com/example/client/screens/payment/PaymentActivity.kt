package com.example.client.screens.payment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.client.R
import com.example.client.app.Constants
import com.example.client.base.BaseActivityMVP
import com.example.client.screens.payment.present.IPaymentPresent
import com.example.client.screens.payment.present.PaymentPresent
import kotlinx.android.synthetic.main.activity_payment.*
import java.text.NumberFormat
import java.util.*


class PaymentActivity : BaseActivityMVP<IPaymentPresent>(), IPaymentView, View.OnClickListener {

    companion object {
        @JvmStatic
        fun newInstance(from: Activity) = Intent(from, PaymentActivity::class.java)
    }
    
    private var mPaymentMethod = Constants.PaymentMethod.COD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
    }

    override val presenter: IPaymentPresent
        get() = PaymentPresent(this)

    override fun bindData() {
        presenter.getPaymentMethod()
        presenter.getAmount()
    }

    override fun bindEvent() {
        radio_cod.setOnClickListener(this)
        radio_wallet.setOnClickListener(this)
        radio_momo.setOnClickListener(this)
        imv_back.setOnClickListener(this)
        tv_submit.setOnClickListener(this)
    }

    override fun showPaymentMethod(paymentMethod: Constants.PaymentMethod) {
        mPaymentMethod = paymentMethod
        when (paymentMethod) {
            Constants.PaymentMethod.COD -> {
                radio_cod.isChecked = true
            }
            Constants.PaymentMethod.MOMO -> {
                radio_momo.isChecked = true
            }
            Constants.PaymentMethod.WALLET -> {
                radio_wallet.isChecked = true
            }
        }
    }

    override fun showAmount(amount: Double) {
        radio_wallet.append(". " + getString(R.string.payment_amount, NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(amount)))
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun onBackPress() {
        onBackPressed()
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.radio_cod -> mPaymentMethod = Constants.PaymentMethod.COD
            R.id.radio_momo -> mPaymentMethod = Constants.PaymentMethod.MOMO
            R.id.radio_wallet -> mPaymentMethod = Constants.PaymentMethod.WALLET
            R.id.tv_submit -> presenter.setPaymentMethod(mPaymentMethod)
            R.id.imv_back -> finish()
        }
    }
}