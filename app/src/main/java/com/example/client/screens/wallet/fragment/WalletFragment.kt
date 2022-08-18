package com.example.client.screens.wallet.fragment

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.client.R
import com.example.client.app.Constants
import com.example.client.base.BaseCollectionFragment
import com.example.client.models.order.OrderModel
import com.example.client.models.profile.ProfileModel
import com.example.client.models.transaction.TransactionModel
import com.example.client.screens.main.activity.MainActivity
import com.example.client.screens.order.detail.OrderDetailActivity
import com.example.client.screens.wallet.item.TransactionItem
import com.example.client.screens.wallet.present.IWalletPresent
import com.example.client.screens.wallet.present.WalletPresent
import com.example.client.screens.wallet.recharge.RechargeActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import kotlinx.android.synthetic.main.fragment_wallet.*
import java.text.NumberFormat
import java.util.*

class WalletFragment : BaseCollectionFragment<IWalletPresent>(), IWalletView, View.OnClickListener {
    private var mItems = arrayListOf<TransactionModel>()

    companion object {
        @JvmStatic
        fun newInstance() = WalletFragment()
    }

    override val presenter: IWalletPresent
        get() = WalletPresent(this)


    override fun bindComponent() {
        tab_transaction.addTab(tab_transaction.newTab().setText(getString(R.string.transaction_label_recharge)))
        tab_transaction.addTab(tab_transaction.newTab().setText(getString(R.string.transaction_label_other)))
    }

    override fun bindData() {
        mPresenter?.run {
            getProfile()
            bindData(Constants.Transaction.RECHARGE)
        }

    }

    override fun bindEvent() {
        tv_recharge.setOnClickListener(this)

        tab_transaction.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position == 0) {
                    mPresenter?.bindData(Constants.Transaction.RECHARGE)
                } else {
                    mPresenter?.bindData(Constants.Transaction.TRANSACTION)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    override fun showWallet(profile: ProfileModel) {
        tv_wallet.text = NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(profile.wallet)
    }

    override fun showData(items: List<TransactionModel>) {
        mItems = items as ArrayList<TransactionModel>
        imv_empty.visibility = View.GONE
        recycler_view.visibility = View.VISIBLE
        val manager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val item = TransactionItem(requireContext(), items) {
            startActivity(OrderDetailActivity.newInstance(requireContext(), it))
        }
        recycler_view.layoutManager = manager
        recycler_view.adapter = item
    }

    override fun showMoreData(items: List<TransactionModel>) {
        mItems.addAll(items)
        recycler_view.adapter?.notifyDataSetChanged()
    }

    override fun showEmptyData() {
        imv_empty.visibility = View.VISIBLE
        recycler_view.visibility = View.GONE
    }

    override fun refreshOrderInfo() {
        if (requireActivity() is MainActivity) {
            (requireActivity() as MainActivity).showOrderInfo()
        }
        onRefresh()
    }

    override fun showErrorMessage(errorMessage: Int) {
        showToastMessage(getString(errorMessage))
    }

    override fun updateData(orderModel: OrderModel) {
        val index = mItems.indexOfFirst { it.order_code == orderModel.order_code }
        mItems[index] =  mItems[index].apply { if (orderModel.status == 4) type = Constants.TransactionType.REFUND }
        recycler_view.adapter?.notifyItemChanged(index)
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

    override fun shouldLoadMore() = true

}