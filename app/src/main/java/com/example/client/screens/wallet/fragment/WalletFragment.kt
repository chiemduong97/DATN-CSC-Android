package com.example.client.screens.wallet.fragment

import android.view.View
import com.example.client.R
import com.example.client.app.Constants
import com.example.client.base.BaseCollectionFragment
import com.example.client.models.profile.ProfileModel
import com.example.client.models.transaction.TransactionModel
import com.example.client.screens.wallet.present.IWalletPresent
import com.example.client.screens.wallet.present.WalletPresent
import com.example.client.screens.wallet.recharge.RechargeActivity
import kotlinx.android.synthetic.main.fragment_wallet.*
import java.text.NumberFormat
import java.util.*

class WalletFragment : BaseCollectionFragment<IWalletPresent>(), IWalletView, View.OnClickListener {

    companion object {
        @JvmStatic
        fun newInstance() = WalletFragment()
    }

    override val presenter: IWalletPresent
        get() = WalletPresent(this)

    override fun bindData() {
        presenter.getProfile()
    }

    override fun bindEvent() {
        tv_recharge.setOnClickListener(this)
    }

    override fun showWallet(profile: ProfileModel) {
        tv_wallet.text = NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(profile.wallet)
    }

    override fun showTransactions(items: List<TransactionModel>, method: Constants.Transaction) {
    }

    override fun showEmptyData() {
        imv_empty.visibility = View.VISIBLE
        recycler_view.visibility = View.GONE
    }

    override fun onBackPress() {
        requireActivity().onBackPressed()
    }

    override fun onClick(v: View) {
        if (v.id == R.id.tv_recharge) {
            startActivity(RechargeActivity.newInstance(requireActivity()))
        }
    }

    override fun getLayout(): Int {
        return R.layout.fragment_wallet
    }

}