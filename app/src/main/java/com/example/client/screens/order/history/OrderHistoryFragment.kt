package com.example.client.screens.order.history

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.client.R
import com.example.client.base.BaseCollectionFragment
import com.example.client.models.order.OrderModel
import com.example.client.screens.order.detail.OrderDetailActivity
import com.example.client.screens.order.history.activity.IOrderHistoryView
import com.example.client.screens.order.history.item.OrderHistoryItem
import com.example.client.screens.order.history.present.IOrderHistoryPresent
import com.example.client.screens.order.history.present.OrderHistoryPresent
import kotlinx.android.synthetic.main.fragment_product.*

class OrderHistoryFragment: BaseCollectionFragment<IOrderHistoryPresent>(), IOrderHistoryView, View.OnClickListener {
    private var mItems = arrayListOf<OrderModel>()
    private val manager by lazy { LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false) }

    companion object {
        @JvmStatic
        fun newInstance() = OrderHistoryFragment()
    }

    override val presenter: IOrderHistoryPresent
        get() = OrderHistoryPresent(this)

    override fun onRefresh() {
        super.onRefresh()
        bindData()
    }

    override fun bindData() {
        presenter.bindData()
    }

    override fun bindEvent() {
        imv_back.setOnClickListener(this)
    }

    override fun showData(items: List<OrderModel>) {
        mItems = items as ArrayList<OrderModel>
        recycler_view.visibility = View.VISIBLE
        imv_empty.visibility = View.GONE
        val item = OrderHistoryItem(requireContext(), mItems) { orderCode ->
            presenter.onClickItem(orderCode)
        }
        recycler_view.layoutManager = manager
        recycler_view.adapter = item
    }

    override fun showMoreData(items: List<OrderModel>) {
        mItems.addAll(items)
        recycler_view.adapter?.notifyDataSetChanged()
    }

    override fun showEmptyData() {
        recycler_view.visibility = View.GONE
        imv_empty.visibility = View.VISIBLE
    }

    override fun showErrorMessage(errMessage: Int) {
        showToastMessage(getString(errMessage))
    }

    override fun goToOrderDetailScreen(orderCode: String) {
        startActivity(OrderDetailActivity.newInstance(requireContext(), orderCode))
    }

    override fun updateData(orderModel: OrderModel) {
        val index = mItems.indexOfFirst { it.order_code == orderModel.order_code }
        mItems[index] = orderModel
        recycler_view.adapter?.notifyItemChanged(index)
    }

    override fun onClick(v: View) {
        if (v.id == R.id.imv_back) {
            requireActivity().onBackPressed()
        }
    }

    override fun getLayout(): Int {
        return R.layout.fragment_order_history
    }

    override fun onBackPress() {
        requireActivity().onBackPressed()
    }

    override fun shouldLoadMore() = true
}