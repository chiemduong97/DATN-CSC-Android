package com.example.client.screens.main.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.example.client.R
import com.example.client.base.BaseActivityMVP
import com.example.client.models.order.OrderModel
import com.example.client.screens.branch.BranchActivity
import com.example.client.screens.cart.activity.CartActivity
import com.example.client.screens.home.fragment.HomeFragment
import com.example.client.screens.main.present.IMainPresent
import com.example.client.screens.main.present.MainPresent
import com.example.client.screens.map.activity.MapsActivity
import com.example.client.screens.order.detail.OrderDetailActivity
import com.example.client.screens.order.history.activity.OrderHistoryActivity
import com.example.client.screens.payment.PaymentActivity
import com.example.client.screens.product.activity.ProductActivity
import com.example.client.screens.profile.fragment.ProfileFragment
import com.example.client.screens.wallet.fragment.WalletFragment
import com.example.client.utils.ActivityUtils
import com.example.client.utils.LocationUtils
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivityMVP<IMainPresent>(), IMainView, View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private var tag = HomeFragment::class.java.name
    private var showOrderCount = false
    private var showOrder = false
    private var showCart = false

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
        requestLocationPermission()
    }

    private fun requestLocationPermission() {
        presenter.run {
            if (LocationUtils.isEnableGPS(this@MainActivity)) {
                LocationUtils.requestLocationPermission(this@MainActivity) {
                    updateCurrentLocation()
                }
            } else {
                val mGoogleApiClient = GoogleApiClient.Builder(this@MainActivity)
                        .addApi(LocationServices.API)
                        .addConnectionCallbacks(this@MainActivity)
                        .addOnConnectionFailedListener(this@MainActivity).build()
                mGoogleApiClient.run {
                    connect()
                    LocationUtils.requestGPS(this, this@MainActivity)
                }
            }
            bindData()
        }
    }


    override fun bindEvent() {
        navigation.setOnItemSelectedListener { item: MenuItem ->
            if (!item.isChecked) {
                when (item.itemId) {
                    R.id.menu_home -> showHomeScreen()
                    R.id.menu_wallet -> showWalletScreen()
                    R.id.menu_profile -> showProfileScreen()
                }
                showOrderInfo()
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

    private fun showWalletScreen() {
        tag = WalletFragment::class.java.simpleName
        ActivityUtils.addFragmentToActivity(supportFragmentManager, WalletFragment.newInstance(), R.id.frame_layout, tag)
    }

    private fun showProfileScreen() {
        tag = ProfileFragment::class.java.simpleName
        ActivityUtils.addFragmentToActivity(supportFragmentManager, ProfileFragment.newInstance(), R.id.frame_layout, tag)
    }

    fun showOrderInfo() {
        if (tag == HomeFragment::class.java.simpleName) {
            cv_cart_place.visibility = if (showCart) View.VISIBLE else View.GONE
            rll_count_order.visibility = if (showOrderCount) View.VISIBLE else View.GONE
            rll_order.visibility = if (showOrder) View.VISIBLE else View.GONE
        } else {
            cv_cart_place.visibility = View.GONE
            rll_count_order.visibility = View.GONE
            rll_order.visibility = View.GONE
        }
    }

    override fun showCart(quantity: Int) {
        showCart = true
        cv_cart_place.visibility = View.VISIBLE
        tv_cart_quantity.text = quantity.toString()
    }

    override fun hideCart() {
        showCart = false
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
        showOrderCount = true
        rll_count_order.visibility = View.VISIBLE
        tv_count_order.text = getString(R.string.text_count_order, count)
    }

    override fun hideOrderCount() {
        showOrderCount = false
        rll_count_order.visibility = View.GONE
    }

    override fun showOrder(order: OrderModel) {
        showOrder = true
        rll_order.visibility = View.VISIBLE
        tv_order_description.text = order.getStatusString().plus("\n").plus(getString(R.string.main_delivery_to)).plus(order.address)
    }

    override fun hideOrder() {
        showOrder = false
        rll_order.visibility = View.GONE
    }

    override fun navigateToOrderDetail(orderCode: String) {
        startActivity(OrderDetailActivity.newInstance(this@MainActivity, orderCode))
    }

    override fun toBranchScreen() {
        startActivity(Intent(this, BranchActivity::class.java))
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.imv_notification -> startActivity(Intent(this, PaymentActivity::class.java))
            R.id.cv_cart_place -> startActivity(Intent(this, CartActivity::class.java))
            R.id.tv_see_more -> startActivity(OrderHistoryActivity.newInstance(this))
            R.id.tv_see_order -> presenter.navigateToOrderDetail()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MapsActivity.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    presenter.updateCurrentLocation()
                } else showErrorMessage(R.string.GPS_not_found)
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onConnected(p0: Bundle?) {
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            LocationUtils.REQUEST_LOCATION -> when (resultCode) {
                RESULT_OK -> {
                    requestLocationPermission()
                }
                else -> showErrorMessage(R.string.GPS_not_found)
            }
        }
    }

}