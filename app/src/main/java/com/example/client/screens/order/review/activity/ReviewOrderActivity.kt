package com.example.client.screens.order.review.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.client.R
import com.example.client.app.Constants
import com.example.client.app.Momo
import com.example.client.base.BaseActivityMVP
import com.example.client.dialog.PrimaryDialog
import com.example.client.models.branch.BranchModel
import com.example.client.models.cart.CartModel
import com.example.client.models.order.OrderLocation
import com.example.client.models.profile.ProfileModel
import com.example.client.screens.branch.BranchActivity
import com.example.client.screens.cart.item.CartProductItem
import com.example.client.screens.map.activity.MapsActivity
import com.example.client.screens.order.detail.OrderDetailActivity
import com.example.client.screens.order.review.present.IReviewOrderPresent
import com.example.client.screens.order.review.present.ReviewOrderPresent
import com.example.client.screens.payment.PaymentActivity
import com.example.client.screens.promotion.activity.PromotionActivity
import com.example.client.utils.MomoCallBack
import kotlinx.android.synthetic.main.activity_review_order.*
import java.text.NumberFormat
import java.util.*
import kotlin.math.roundToInt

class ReviewOrderActivity : BaseActivityMVP<IReviewOrderPresent>(), IReviewOrderView, View.OnClickListener {

    companion object {
        fun newInstance(context: Context): Intent {
            return Intent(context, ReviewOrderActivity::class.java)
        }

        fun newInstance(context: Context, isReOrder: Boolean, orderCode: String) = Intent(context, ReviewOrderActivity::class.java).apply {
            val bundle = Bundle().apply {
                putBoolean(Constants.BundleKey.REORDER, isReOrder)
                putString(Constants.BundleKey.ORDER_CODE, orderCode)
            }
            putExtras(bundle)
        }

    }

    override val presenter: IReviewOrderPresent
        get() = ReviewOrderPresent(this)

    private val isReOrder by lazy { intent.getBooleanExtra(Constants.BundleKey.REORDER, false) }
    private val orderCode by lazy { intent.getStringExtra(Constants.BundleKey.ORDER_CODE).orEmpty() }

    private val paymentMethods by lazy {
        arrayListOf(
                Pair(R.drawable.ic_payment_cod, R.string.payment_method_cod),
                Pair(R.drawable.ic_payment_momo, R.string.payment_method_momo),
                Pair(R.drawable.ic_payment_wallet, R.string.payment_method_wallet)
        )
    }

    private var paymentMethod = Constants.PaymentMethod.COD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_order)
    }

    override fun bindData() {
        presenter.binData(isReOrder, orderCode)
    }

    private fun bindPaymentMethod(paymentMethod: Pair<Int, Int>, amount: Double) {
        if (paymentMethod == paymentMethods[2]) {
            tv_amount.visibility = View.VISIBLE
            tv_amount.text = getString(R.string.payment_amount, NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(amount))
        } else tv_amount.visibility = View.GONE
        imv_payment_method.setImageDrawable(ContextCompat.getDrawable(this, paymentMethod.first))
        tv_payment_method.text = getString(paymentMethod.second)
    }

    private fun getPaymentMethod(paymentMethod: Constants.PaymentMethod): Pair<Int, Int> = when (paymentMethod) {
        Constants.PaymentMethod.COD -> paymentMethods[0]
        Constants.PaymentMethod.MOMO -> paymentMethods[1]
        Constants.PaymentMethod.WALLET -> paymentMethods[2]
    }

    override fun bindEvent() {
        imv_back.setOnClickListener(this)
        tv_change_branch.setOnClickListener(this)
        tv_change_product.setOnClickListener(this)
        tv_send_order.setOnClickListener(this)
        tv_change_order_location.setOnClickListener(this)
        tv_change_payment.setOnClickListener(this)
        lnl_add_promotion.setOnClickListener(this)
        imv_remove_promotion.setOnClickListener(this)
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.imv_back, R.id.tv_change_product -> onBackPressed()
            R.id.tv_change_branch -> startActivity(BranchActivity.newInstance(this))
            R.id.tv_send_order -> {
                when (paymentMethod) {
                    Constants.PaymentMethod.COD -> presenter.createOrder()
                    Constants.PaymentMethod.WALLET -> presenter.createOrderWithWallet()
                    Constants.PaymentMethod.MOMO -> presenter.requestMomo()
                }
            }
            R.id.tv_change_order_location -> startActivity(MapsActivity.newInstance(this))
            R.id.tv_change_payment -> startActivity(PaymentActivity.newInstance(this))
            R.id.lnl_add_promotion -> startActivity(PromotionActivity.newInstance(this))
            R.id.imv_remove_promotion -> presenter.removePromotion()
        }
    }

    override fun showUser(profile: ProfileModel) {
        tv_profile_name.text = profile.fullname
        Glide.with(this).asBitmap().placeholder(R.drawable.avatar_default).load(profile.avatar).into(imv_profile_avatar)
    }

    override fun showOrderLocation(orderLocation: OrderLocation) {
        tv_order_address.text = orderLocation.address
    }

    override fun showBranch(branch: BranchModel) {
        branch.apply {
            tv_branch_name.text = name
            tv_branch_address.text = address
        }
    }

    override fun showCartProduct(cart: CartModel) {
        tv_shipping_fee_price.text = NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(cart.getShippingFeeExpect())
        cart.cartProducts.let {
            if (it.isEmpty()) {
                onBackPressed()
                return
            }
            val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            val item = CartProductItem(this, it, { cartProduct ->
                presenter.plus(cartProduct)
            }, { cartProduct ->
                presenter.minus(cartProduct)
            })
            recyclerView.layoutManager = manager
            recyclerView.adapter = item
        }
        updateTotalPrice(cart)
    }

    override fun showLoading() {
        rll_loading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        rll_loading.visibility = View.GONE
    }

    override fun showErrorMessage(errMessage: Int) {
        PrimaryDialog({}, {})
                .setDescription(getString(errMessage))
                .show(supportFragmentManager)
    }

    override fun createOrderSuccess(orderCode: String) {
        PrimaryDialog({
            finish()
            startActivity(OrderDetailActivity.newInstance(this, orderCode))
        }, { })
                .showBtnCancel(false)
                .setDescription(getString(R.string.create_order_success))
                .show(supportFragmentManager)

    }

    override fun onBackPress() {
        super.onBackPressed()
    }

    override fun updateTotalPrice(cart: CartModel) {
        tv_total_price.text = NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(if (cart.getTotalPrice() < 0) 0 else cart.getTotalPrice())
    }

    override fun updatePaymentMethod(paymentMethod: Constants.PaymentMethod, amount: Double) {
        bindPaymentMethod(getPaymentMethod(paymentMethod), amount)
        this.paymentMethod = paymentMethod
    }

    override fun updatePromotion(cart: CartModel) {
        cart.promotion_id?.let {
            lnl_add_promotion.background = ContextCompat.getDrawable(this, R.drawable.border_item_green_5)
            imv_remove_promotion.visibility = View.VISIBLE
            rll_promotion.visibility = View.VISIBLE
            tv_promotion_code.text = cart.promotion_code
            tv_promotion_value.text = NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(
                    cart.promotion_value?.let {
                        if (it < 1) (cart.getAmount() * it / 1000).roundToInt() * 1000.0 else it
                    }
            )
        } ?: kotlin.run {
            lnl_add_promotion.background = ContextCompat.getDrawable(this, R.drawable.border_item_gray_5)
            imv_remove_promotion.visibility = View.GONE
            rll_promotion.visibility = View.GONE
        }
        updateTotalPrice(cart)
    }

    override fun requestMomo(cart: CartModel) {
        Momo.newInstance().requestMoMo(this, cart.getTotalPrice().toInt())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) Momo.newInstance().callbackResult(requestCode, data, object : MomoCallBack {
            override fun onSuccess(data: String, userPhone: String) {
                presenter.createOrderWithMomo(userPhone, data)
            }

            override fun onError() {
                showDialogErrorMessage(getString(R.string.err_code_1001))
            }

        })
    }
}