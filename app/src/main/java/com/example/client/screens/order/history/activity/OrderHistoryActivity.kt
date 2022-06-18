package com.example.client.screens.order.history.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.client.R
import com.example.client.app.Constants
import com.example.client.dialog.PrimaryDialog
import com.example.client.models.event.Event
import com.example.client.models.order.OrderModel
import com.example.client.screens.order.detail.OrderDetailActivity
import com.example.client.screens.order.history.item.OrderHistoryItem
import com.example.client.screens.order.history.present.OrderHistoryPresent
import kotlinx.android.synthetic.main.activity_order_history.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class OrderHistoryActivity : AppCompatActivity(), IOrderHistoryView, View.OnClickListener{
    var present: OrderHistoryPresent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history)
        present = OrderHistoryPresent(this)
        present?.getListOrderFromService()
        imv_back.setOnClickListener(this)
    }

    override fun showData(list: List<OrderModel>) {
        recyclerView.visibility = View.VISIBLE
        imv_empty.visibility = View.GONE
        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val item = OrderHistoryItem(this, list) {
            startActivity(OrderDetailActivity.newInstance(this, it))
        }
        recyclerView.layoutManager = manager
        recyclerView.adapter = item
    }

    override fun showLoading() {
        rll_loading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        rll_loading.visibility = View.GONE
    }

    override fun showEmpty() {
        recyclerView.visibility = View.GONE
        imv_empty.visibility = View.VISIBLE
    }

    override fun showErrorMessage(errMessage: Int) {
        PrimaryDialog({}, {})
                .setDescription(getString(errMessage))
                .show(supportFragmentManager)
    }

    override fun onClick(v: View?) {
        v?.let {
            when (it.id) {
                R.id.imv_back -> {
                    onBackPressed()
                    finish()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: Event) {
        when (event.key) {
            Constants.EventKey.UPDATE_STATUS_ORDER -> present?.getListOrderFromService()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}