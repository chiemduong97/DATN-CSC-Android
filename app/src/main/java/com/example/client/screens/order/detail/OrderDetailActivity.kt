package com.example.client.screens.order.detail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.client.R
import com.example.client.app.Constants
import com.example.client.base.BaseActivityMVP
import com.example.client.dialog.PrimaryDialog
import com.example.client.models.branch.BranchModel
import com.example.client.models.order.OrderDetailModel
import com.example.client.models.order.OrderModel
import com.example.client.screens.order.detail.item.OrderDetailItem
import com.example.client.screens.order.detail.present.IOrderDetailPresent
import com.example.client.screens.order.detail.present.OrderDetailPresent
import com.example.client.screens.order.review.activity.ReviewOrderActivity
import kotlinx.android.synthetic.main.activity_order_detail.*
import java.text.NumberFormat
import java.util.*
import kotlin.math.roundToInt

class OrderDetailActivity : BaseActivityMVP<IOrderDetailPresent>(), IOrderDetailView, View.OnClickListener {

    companion object {
        fun newInstance(context: Context, orderCode: String) = Intent(context, OrderDetailActivity::class.java).apply {
            val bundle = Bundle().apply {
                putString(Constants.BundleKey.ORDER_CODE, orderCode)
            }
            putExtras(bundle)
        }
    }

    override val presenter: IOrderDetailPresent
        get() = OrderDetailPresent(this)

    private val orderCode by lazy { intent.getStringExtra(Constants.BundleKey.ORDER_CODE).orEmpty() }

    private val paymentMethods by lazy {
        arrayListOf(
                Pair(R.drawable.ic_payment_cod, R.string.payment_method_cod),
                Pair(R.drawable.ic_payment_momo, R.string.payment_method_momo),
                Pair(R.drawable.ic_payment_wallet, R.string.payment_method_wallet)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)
    }

    override fun bindData() {
        presenter.run {
            getOrder(orderCode)
            getOrderDetails(orderCode)
        }
    }

    override fun bindEvent() {
        imv_back.setOnClickListener(this)
        tv_order_destroy.setOnClickListener(this)
        rll_re_order.setOnClickListener(this)
    }

    private fun bindPaymentMethod(paymentMethod: Pair<Int, Int>) {
        imv_payment_method.setImageDrawable(ContextCompat.getDrawable(this, paymentMethod.first))
        tv_payment_method.text = getString(paymentMethod.second)
    }

    private fun getPaymentMethod(paymentMethod: Constants.PaymentMethod): Pair<Int, Int> = when (paymentMethod) {
        Constants.PaymentMethod.COD -> paymentMethods[0]
        Constants.PaymentMethod.MOMO -> paymentMethods[1]
        Constants.PaymentMethod.WALLET -> paymentMethods[2]
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

    override fun showOrderDetail(order: OrderModel) {
        tv_order_address.text = order.address
        tv_branch_address.text = order.branch_address
        tv_shipping_fee_price.text = NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(order.shipping_fee)
        if (order.promotion_id != -1) {
            rll_promotion.visibility = View.VISIBLE
            tv_promotion_code.text = order.promotion_code
            tv_promotion_value.text = NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(
                    if (order.promotion_value < 1) (order.amount * order.promotion_value / 1000).roundToInt() * 1000.0 else order.promotion_value
            )
        }
        tv_order_code.text = getString(R.string.text_order_code, order.order_code)
        tv_title.text = getString(R.string.text_title_order_detail, order.order_code)
        tv_total_price.text = NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(order.getTotalPrice())
        if (order.payment_method != Constants.PaymentMethod.COD) {
            tv_status_payment.text = getString(R.string.payment_paid)
            tv_status_payment.visibility = View.VISIBLE
        } else {
            tv_status_payment.visibility = View.GONE
        }
        when {
            order.isComplete() -> {
                status_order_done.visibility = View.VISIBLE
                status_order_destroy.visibility = View.GONE
                sb_layout.visibility = View.GONE
                rll_re_order.visibility = View.VISIBLE
            }
            order.isDestroy() -> {
                if (order.payment_method != Constants.PaymentMethod.COD) {
                    tv_status_payment.text = getString(R.string.payment_refund)
                    tv_status_payment.visibility = View.VISIBLE
                } else {
                    tv_status_payment.visibility = View.GONE
                }
                status_order_done.visibility = View.GONE
                status_order_destroy.visibility = View.VISIBLE
                sb_layout.visibility = View.GONE
                rll_re_order.visibility = View.VISIBLE
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
        bindPaymentMethod(getPaymentMethod(order.payment_method))

    }

    override fun showBranch(branch: BranchModel) {
        tv_branch_name.text = branch.name
        tv_branch_address.text = branch.address
    }

    override fun showLoading() {
        rll_loading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        rll_loading.visibility = View.GONE
    }

    override fun onBackPress() {
        super.onBackPressed()
    }

    override fun showErrorMessage(errMessage: Int) {
        PrimaryDialog({}, {})
                .setDescription(getString(errMessage))
                .show(supportFragmentManager)
    }

    override fun showProducts(products: List<OrderDetailModel>) {
        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val item = OrderDetailItem(this, products)
        recyclerView.layoutManager = manager
        recyclerView.adapter = item
    }

    override fun onRefresh() {
        bindData()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.imv_back -> {
                onBackPressed()
                finish()
            }
            R.id.tv_order_destroy -> {
                PrimaryDialog({
                    presenter.destroyOrder(orderCode)
                }, {})
                        .setDescription(getString(R.string.destroy_order_sure))
                        .show(supportFragmentManager)
            }
            R.id.rll_re_order -> startActivity(ReviewOrderActivity.newInstance(this, true, orderCode))
        }
    }


}