package com.example.client.screens.order.detail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.client.R
import com.example.client.app.Constants
import com.example.client.dialog.PrimaryDialog
import com.example.client.models.branch.BranchModel
import com.example.client.models.event.Event
import com.example.client.models.order.OrderDetailModel
import com.example.client.models.order.OrderModel
import com.example.client.screens.order.detail.item.OrderDetailItem
import com.example.client.screens.order.detail.present.OrderDetailPresent
import kotlinx.android.synthetic.main.activity_order_detail.*
import kotlinx.android.synthetic.main.activity_order_detail.recyclerView
import kotlinx.android.synthetic.main.activity_order_detail.rll_loading
import kotlinx.android.synthetic.main.activity_order_detail.rll_promotion
import kotlinx.android.synthetic.main.activity_order_detail.tv_branch_address
import kotlinx.android.synthetic.main.activity_order_detail.tv_branch_name
import kotlinx.android.synthetic.main.activity_order_detail.tv_order_address
import kotlinx.android.synthetic.main.activity_order_detail.tv_promotion_code
import kotlinx.android.synthetic.main.activity_order_detail.tv_promotion_value
import kotlinx.android.synthetic.main.activity_order_detail.tv_shipping_fee_price
import kotlinx.android.synthetic.main.activity_order_detail.tv_total_price
import kotlinx.android.synthetic.main.activity_order_detail.imv_back
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.NumberFormat
import java.util.*

class OrderDetailActivity : AppCompatActivity(), IOrderDetailView, View.OnClickListener {

    private var dialog: PrimaryDialog? = null
    private var present: OrderDetailPresent? = null
    var ordercode = ""
    var orderStatus = -1

    companion object {
        fun newInstance(context: Context,ordercode: String) : Intent {
            return Intent(context, OrderDetailActivity::class.java).apply {
                val bundle = Bundle().apply {
                    putString(Constants.ORDERCODE, ordercode)
                }
                putExtras(bundle)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)

        imv_back.setOnClickListener(this)
        tv_order_destroy.setOnClickListener(this)

        ordercode = intent.getStringExtra(Constants.ORDERCODE) ?: "ABC123"
        dialog = PrimaryDialog()
        dialog?.getInstance(this)

        present = OrderDetailPresent(this)

        present?.let {
            it.getOrderFromService(ordercode)
            it.getListOrderDetailFromService(ordercode)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initSeekBar(status: Int, statusString: String) {
        val view = LayoutInflater.from(this).inflate(R.layout.item_status_order, null)
        val tvStatusOrder: TextView = view.findViewById(R.id.tv_status_order)
        tvStatusOrder.text = statusString
        seek_bar_status.indicator.topContentView = view
        seek_bar_status.setProgress(status.toFloat())
        seek_bar_status.setOnTouchListener { _, _ -> true }
    }

    override fun initView(order: OrderModel) {
        orderStatus = order.status
        tv_order_address.text = order.address
        tv_branch_address.text = order.branch_address
        tv_shipping_fee_price.text = NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(order.shippingFee)
        order.promotion_id?.let {
            rll_promotion.visibility = View.VISIBLE
            tv_promotion_code.text = order.promotionCode
            tv_promotion_value.text = NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(order.promotionValue)
        } ?: kotlin.run {
            rll_promotion.visibility = View.GONE
        }
        tv_order_code.text = getString(R.string.text_order_code).replace("%s", order.ordercode)
        tv_title.text = getString(R.string.text_title_order_detail).replace("%s", order.ordercode)
        tv_total_price.text = NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(order.getTotalPrice())
        when {
            order.isComplete() -> {
                status_order_done.visibility = View.VISIBLE
                status_order_destroy.visibility = View.GONE
                sb_layout.visibility = View.GONE
            }
            order.isDestroy() -> {
                status_order_done.visibility = View.GONE
                status_order_destroy.visibility = View.VISIBLE
                sb_layout.visibility = View.GONE
            }
            else -> {
                status_order_done.visibility = View.GONE
                status_order_destroy.visibility = View.GONE
                sb_layout.visibility = View.VISIBLE
                initSeekBar(order.status, order.getStatusString())
            }
        }
        if (order.isWaiting()) {
            tv_order_destroy.visibility = View.VISIBLE
        } else {
            tv_order_destroy.visibility = View.GONE
        }

    }

    override fun showBranchOrder(branch: BranchModel) {
        tv_branch_name.text = branch.name
    }

    override fun showLoading() {
        rll_loading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        rll_loading.visibility = View.GONE
    }

    override fun showErrorMessage(errMessage: Int) {
        dialog?.apply {
            setDescription(getString(errMessage))
            setOKListener {}
            hideBtnCancel()
            show()
        }
    }

    override fun showListProduct(products: List<OrderDetailModel>) {
        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val item = OrderDetailItem(this, products)
        recyclerView.layoutManager = manager
        recyclerView.adapter = item
    }

    override fun onRefresh() {
        present?.let {
            it.getOrderFromService(ordercode)
            it.getListOrderDetailFromService(ordercode)
        }
    }

    override fun onClick(v: View?) {
        v?.let {
            when (v.id) {
                R.id.imv_back -> {
                    onBackPressed()
                    finish()
                }
                R.id.tv_order_destroy -> {
                    dialog?.apply {
                        setDescription(getString(R.string.destroy_order_sure))
                        setOKListener {
                            present?.destroyOrder(ordercode, orderStatus)
                        }
                        setCancelListener {  }
                        show()
                    }
                }
                else -> {

                }
            }
        }
    }
}