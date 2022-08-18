package com.example.client.screens.notify

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.client.R
import com.example.client.base.BaseCollectionFragment
import com.example.client.models.notify.NotifyModel
import com.example.client.screens.notify.item.NotifyItem
import com.example.client.screens.notify.present.INotifyPresent
import com.example.client.screens.notify.present.NotifyPresent
import com.example.client.screens.order.detail.OrderDetailActivity
import kotlinx.android.synthetic.main.fragment_notify.*

class NotifyFragment: BaseCollectionFragment<INotifyPresent>(), INotifyView, View.OnClickListener {
    private var mItems = arrayListOf<NotifyModel>()
    private val manager by lazy { LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false) }

    companion object {
        @JvmStatic
        fun newInstance() = NotifyFragment()
    }

    override val presenter: INotifyPresent
        get() = NotifyPresent(this)

    override fun bindData() {
        mPresenter?.bindData()
    }

    override fun bindEvent() {
        imv_back.setOnClickListener(this)
    }

    override fun showData(items: List<NotifyModel>) {
        mItems = items as ArrayList<NotifyModel>
        recycler_view.visibility = View.VISIBLE
        imv_empty.visibility = View.GONE
        val item = NotifyItem(requireContext(), mItems) { orderCode ->
            startActivity(OrderDetailActivity.newInstance(requireContext(), orderCode))
        }
        recycler_view.layoutManager = manager
        recycler_view.adapter = item
    }

    override fun showMoreData(items: List<NotifyModel>) {
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

    override fun onClick(v: View) {
        if (v.id == R.id.imv_back) {
            requireActivity().onBackPressed()
        }
    }

    override fun getLayout(): Int {
        return R.layout.fragment_notify
    }

    override fun onBackPress() {
        requireActivity().onBackPressed()
    }

    override fun shouldLoadMore() = true
}