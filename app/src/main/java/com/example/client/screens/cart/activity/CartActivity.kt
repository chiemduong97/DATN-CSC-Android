package com.example.client.screens.cart.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.client.R
import com.example.client.base.BaseActivityMVP
import com.example.client.models.branch.BranchModel
import com.example.client.models.cart.CartModel
import com.example.client.models.profile.ProfileModel
import com.example.client.screens.branch.BranchActivity
import com.example.client.screens.cart.item.CartProductItem
import com.example.client.screens.cart.present.CartPresent
import com.example.client.screens.cart.present.ICartPresent
import com.example.client.screens.order.review.activity.ReviewOrderActivity
import kotlinx.android.synthetic.main.activity_cart.*
import java.text.NumberFormat
import java.util.*

class CartActivity : BaseActivityMVP<ICartPresent>(), ICartView, View.OnClickListener {
    companion object{
        fun newInstance(from: Activity) : Intent{
            return Intent(from, CartActivity::class.java)
        }
    }

    override val presenter: ICartPresent
        get() = CartPresent(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
    }

    override fun bindData() {
        presenter.bindData()
    }

    override fun bindEvent() {
        rll_change_branch.setOnClickListener(this)
        imv_back.setOnClickListener(this)
        tv_order.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        v?.let {
            when (it.id) {
                R.id.rll_change_branch -> {
                    startActivity(Intent(this, BranchActivity::class.java))
                }
                R.id.imv_back -> {
                    onBackPressed()
                }
                R.id.tv_order -> {
                    finish()
                    startActivity(ReviewOrderActivity.newInstance(this))
                }
                else -> {

                }
            }
        }
    }

    override fun showBranchInfo(branch: BranchModel) {
        branch.apply {
            tv_branch_name.text = name
            tv_branch_address.text = address
        }
    }

    override fun showUserInfo(profile: ProfileModel) {
        tv_profile_name.text = profile.fullname
        Glide.with(this).asBitmap().placeholder(R.drawable.avatar_default).load(profile.avatar).into(view_icon)
    }

    override fun showCartProduct(cart: CartModel) {
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
            recycler_view.layoutManager = manager
            recycler_view.adapter = item
        }
        updateTotalPrice(cart)
    }

    override fun updateTotalPrice(cart: CartModel) {
        tv_total_price.text = NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(cart.getAmount())
    }

    override fun showLoading() {}

    override fun hideLoading() {}

    override fun onBackPress() {}

}