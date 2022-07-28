package com.example.client.screens.wallet.recharge

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.example.client.R
import com.example.client.app.Momo
import com.example.client.base.BaseActivityMVP
import com.example.client.dialog.PrimaryDialog
import com.example.client.screens.profile.navigate.NavigatorProfile
import com.example.client.screens.wallet.recharge.present.IRechargePresent
import com.example.client.screens.wallet.recharge.present.RechargePresent
import com.example.client.utils.MomoCallBack
import kotlinx.android.synthetic.main.activity_recharge.*
import java.text.NumberFormat
import java.util.*

class RechargeActivity : BaseActivityMVP<IRechargePresent>(), View.OnClickListener, IRechargeView {

    companion object {
        @JvmStatic
        fun newInstance(from: Activity) = Intent(from, RechargeActivity::class.java)
    }

    override val presenter: IRechargePresent
        get() = RechargePresent(this)

    private var amount = 0.0
    private val sections by lazy { arrayListOf(1000000.0, 2000000.0, 5000000.0) }
    private val formatVND by lazy { NumberFormat.getCurrencyInstance(Locale("vi", "VN")) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recharge)
    }

    override fun bindComponent() {
        tv_section_first.text = formatVND.format(sections[0])
        tv_section_second.text = formatVND.format(sections[1])
        tv_section_thirst.text = formatVND.format(sections[2])
    }

    override fun bindEvent() {
        tv_section_first.setOnClickListener(this)
        tv_section_second.setOnClickListener(this)
        tv_section_thirst.setOnClickListener(this)
        imv_back.setOnClickListener(this)
        tv_submit.setOnClickListener(this)
    }

    override fun showToast() {
        showToastMessage("Thành công")
    }

    override fun showErrorMessage(errorMessage: Int) {
        showDialogErrorMessage(getString(errorMessage))
    }

    override fun rechargeSuccess() {
        PrimaryDialog({
            finish()
        }, { })
                .setDescription(getString(R.string.recharge_success))
                .show(supportFragmentManager)
    }


    override fun showLoading() {
        rll_loading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        rll_loading.visibility = View.GONE

    }

    override fun onBackPress() {
        onBackPressed()
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.imv_back -> onBackPressed()
            R.id.tv_submit -> Momo.newInstance().requestMoMo(this, amount.toInt())
            R.id.tv_section_first -> selectSection(0)
            R.id.tv_section_second -> selectSection(1)
            R.id.tv_section_thirst -> selectSection(2)
        }
    }

    private fun selectSection(index: Int) {
        when (index) {
            0 -> {
                tv_section_first.setBackgroundResource(R.drawable.bg_btn)
                tv_section_first.setTextColor(Color.WHITE)
                tv_section_second.setBackgroundResource(R.drawable.border_item)
                tv_section_second.setTextColor(Color.BLACK)
                tv_section_thirst.setBackgroundResource(R.drawable.border_item)
                tv_section_thirst.setTextColor(Color.BLACK)
            }
            1 -> {
                tv_section_first.setBackgroundResource(R.drawable.border_item)
                tv_section_first.setTextColor(Color.BLACK)
                tv_section_second.setBackgroundResource(R.drawable.bg_btn)
                tv_section_second.setTextColor(Color.WHITE)
                tv_section_thirst.setBackgroundResource(R.drawable.border_item)
                tv_section_thirst.setTextColor(Color.BLACK)
            }
            2 -> {
                tv_section_first.setBackgroundResource(R.drawable.border_item)
                tv_section_first.setTextColor(Color.BLACK)
                tv_section_second.setBackgroundResource(R.drawable.border_item)
                tv_section_second.setTextColor(Color.BLACK)
                tv_section_thirst.setBackgroundResource(R.drawable.bg_btn)
                tv_section_thirst.setTextColor(Color.WHITE)
            }
        }
        tv_submit.setBackgroundResource(R.drawable.bg_btn)
        tv_submit.isEnabled = true
        amount = sections[index]
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) Momo.newInstance().callbackResult(requestCode, data, object : MomoCallBack {
            override fun onSuccess(data: String, userPhone: String) {
                presenter.createRequest(amount, userPhone, data)
            }

            override fun onError() {
                showDialogErrorMessage(getString(R.string.err_code_1001))
            }

        })
    }


//    fun toHash(json: String):String  {
//        var encoded = ""
//        try {
//            val publicBytes: ByteArray = Base64.decode(Constants.MoMoConstants.PUBLIC_KEY, Base64.DEFAULT)
//            val keySpec = X509EncodedKeySpec(publicBytes)
//            val keyFactory = KeyFactory.getInstance("RSA")
//            val pubKey = keyFactory.generatePublic(keySpec)
//            val cipher: Cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING") //or try with "RSA"
//            cipher.init(Cipher.ENCRYPT_MODE, pubKey)
//            encoded = Base64.encodeToString(cipher.doFinal(json.toByteArray()), Base64.DEFAULT)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return encoded
//    }
}