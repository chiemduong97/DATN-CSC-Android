package com.example.client.screens.main.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.example.client.R
import com.example.client.base.BaseActivityMVP
import com.example.client.models.order.OrderModel
import com.example.client.screens.cart.activity.CartActivity
import com.example.client.screens.home.fragment.HomeFragment
import com.example.client.screens.main.present.IMainPresent
import com.example.client.screens.main.present.MainPresent
import com.example.client.screens.payment.PaymentActivity
import com.example.client.screens.order.detail.OrderDetailActivity
import com.example.client.screens.order.history.activity.OrderHistoryActivity
import com.example.client.screens.product.activity.ProductActivity
import com.example.client.screens.profile.fragment.ProfileFragment
import com.example.client.screens.wallet.fragment.WalletFragment
import com.example.client.utils.ActivityUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivityMVP<IMainPresent>(), IMainView, View.OnClickListener {

    private var tag = HomeFragment::class.java.name

    companion object {
        @JvmStatic
        fun newInstance(from: Activity): Intent = Intent(from, ProductActivity::class.java)
    }

    override val presenter: IMainPresent
        get() = MainPresent(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        navigation.getOrCreateBadge(R.id.menu_noti).setVisible(true);
//        navigation.getOrCreateBadge(R.id.menu).setNumber(99);
    }

    override fun bindComponent() {
        showHomeScreen()
    }

    override fun bindData() {
        presenter.run {
            updateCurrentLocation()
            bindData()
        }
    }


    override fun bindEvent() {
        navigation.setOnItemSelectedListener { item: MenuItem ->
            if (!item.isChecked) {
                when (item.itemId) {
                    R.id.menu_home -> showHomeScreen()
                    R.id.menu_wallet -> showNotificationScreen()
                    R.id.menu_profile -> showProfileScreen()
                }
            }
            true
        }
        imv_notification.setOnClickListener(this)
        cv_cart_place.setOnClickListener(this)
        tv_see_more.setOnClickListener(this)
        tv_see_order.setOnClickListener(this)
    }

    private fun showHomeScreen() {
        tag = HomeFragment::class.java.simpleName
        ActivityUtils.addFragmentToActivity(supportFragmentManager, HomeFragment.newInstance(), R.id.frame_layout, tag)
    }

    private fun showNotificationScreen() {
        tag = WalletFragment::class.java.simpleName
        ActivityUtils.addFragmentToActivity(supportFragmentManager, WalletFragment.newInstance(), R.id.frame_layout, tag)
    }

    private fun showProfileScreen() {
        tag = ProfileFragment::class.java.simpleName
        ActivityUtils.addFragmentToActivity(supportFragmentManager, ProfileFragment.newInstance(), R.id.frame_layout, tag)
    }

    override fun showCart(quantity: Int) {
        cv_cart_place.visibility = View.VISIBLE
        tv_quantity.text = quantity.toString()
    }

    override fun hideCart() {
        cv_cart_place.visibility = View.GONE
    }

    override fun showLoading() {
        rll_loading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        rll_loading.visibility = View.GONE
    }

    override fun onBackPress() {}

    override fun onBackPressed() {
        finish()
    }

    override fun showErrorMessage(errMessage: Int) {
        showToastMessage(getString(errMessage))
    }

    override fun showOrderCount(count: Int) {
        rll_count_order.visibility = View.VISIBLE
        tv_count_order.text = getString(R.string.text_count_order, count)
    }

    override fun hideOrderCount() {
        rll_count_order.visibility = View.GONE
    }

    override fun showOrder(order: OrderModel) {
        rll_order.visibility = View.VISIBLE
        tv_order_description.text = order.getStatusString().plus("\n").plus(getString(R.string.main_delivery_to)).plus(order.address)
    }

    override fun hideOrder() {
        rll_order.visibility = View.GONE
    }

    override fun navigateToOrderDetail(orderCode: String) {
        startActivity(OrderDetailActivity.newInstance(this@MainActivity, orderCode))
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.imv_notification -> startActivity(Intent(this, PaymentActivity::class.java))
            R.id.cv_cart_place -> startActivity(Intent(this, CartActivity::class.java))
            R.id.tv_see_more -> startActivity(OrderHistoryActivity.newInstance(this))
            R.id.tv_see_order -> presenter.navigateToOrderDetail()
        }
    }

}