package com.example.client.screens.order.review.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.client.R
import com.example.client.base.BaseActivityMVP
import com.example.client.dialog.PrimaryDialog
import com.example.client.models.branch.BranchModel
import com.example.client.models.cart.CartModel
import com.example.client.models.profile.ProfileModel
import com.example.client.screens.branch.BranchActivity
import com.example.client.screens.cart.item.CartProductItem
import com.example.client.screens.map.activity.MapsActivity
import com.example.client.screens.order.detail.OrderDetailActivity
import com.example.client.screens.order.review.present.IReviewOrderPresent
import com.example.client.screens.order.review.present.ReviewOrderPresent
import kotlinx.android.synthetic.main.activity_review_order.*
import java.text.NumberFormat
import java.util.*

class ReviewOrderActivity : BaseActivityMVP<IReviewOrderPresent>(), IReviewOrderView, View.OnClickListener {

    companion object {
        fun newInstance(context: Context?): Intent {
            return Intent(context, ReviewOrderActivity::class.java)
        }
    }

    override val presenter: IReviewOrderPresent
        get() = ReviewOrderPresent(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_order)
    }

    override fun bindData() {
        presenter.binData()
    }

    override fun bindEvent() {
        imv_back.setOnClickListener(this)
        tv_change_branch.setOnClickListener(this)
        tv_change_product.setOnClickListener(this)
        tv_send_order.setOnClickListener(this)
        tv_change_order_location.setOnClickListener(this)
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.imv_back, R.id.tv_change_product -> onBackPressed()
            R.id.tv_change_branch -> startActivity(BranchActivity.newInstance(this))
            R.id.tv_send_order -> presenter.createOrder()
            R.id.tv_change_order_location -> startActivity(MapsActivity.newInstance(this))
        }
    }

    override fun showUser(profile: ProfileModel) {
        tv_profile_name.text = profile.fullname
        tv_order_address.text = profile.address
        Glide.with(this).asBitmap().placeholder(R.drawable.avatar_default).load(profile.avatar).into(imv_profile_avatar)
    }

    override fun showBranch(branch: BranchModel) {
        branch.apply {
            tv_branch_name.text = name
            tv_branch_address.text = address
        }
    }

    override fun showCartProduct(cart: CartModel) {
        tv_shipping_fee_price.text = NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(cart.getShippingFeeExpect())
        tv_total_price.text = NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(cart.getTotalPrice())
        cart.cartProducts.let {
            if (it.isEmpty()) {
                onBackPressed()
                finish()
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

    override fun toOrderDetailScreen(orderCode: String) {
        finish()
        startActivity(OrderDetailActivity.newInstance(this, orderCode))
    }

    override fun onBackPress() {
        super.onBackPressed()
    }

    override fun updateTotalPrice(cart: CartModel) {
        tv_total_price.text = NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(cart.getTotalPrice())
    }

}