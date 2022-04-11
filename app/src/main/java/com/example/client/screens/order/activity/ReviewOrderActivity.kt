package com.example.client.screens.order.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.client.R
import com.example.client.app.Constants
import com.example.client.dialog.PrimaryDialog
import com.example.client.models.branch.BranchModel
import com.example.client.models.cart.CartModel
import com.example.client.models.event.Event
import com.example.client.models.profile.ProfileModel
import com.example.client.screens.branch.BranchActivity
import com.example.client.screens.cart.item.CartProductItem
import com.example.client.screens.order.detail.OrderDetailActivity
import com.example.client.screens.order.present.ReviewOrderPresent
import kotlinx.android.synthetic.main.activity_review_order.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.NumberFormat
import java.util.*

class ReviewOrderActivity : AppCompatActivity(), IReviewOrderView, View.OnClickListener {
    private var cart: CartModel? = null
    private var present: ReviewOrderPresent? = null
    private var dialog: PrimaryDialog? = null

    companion object {
        fun newInstance(context: Context?): Intent {
            return Intent(context, ReviewOrderActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_order)

        imv_back.setOnClickListener(this)
        tv_change_branch.setOnClickListener(this)
        tv_change_product.setOnClickListener(this)
        tv_send_order.setOnClickListener(this)

        dialog = PrimaryDialog()
        dialog?.getInstance(this)

        present = ReviewOrderPresent(this)
        present?.let {
            it.getBranchFromRes()
            it.getUserFromRes()
            it.getCartFromRes()
        }

    }

    override fun onClick(v: View?) {
        v?.let {
            when (v.id) {
                R.id.imv_back -> {
                    onBackPressed()
                    finish()
                }
                R.id.tv_change_product -> {
                    onBackPressed()
                    finish()
                }
                R.id.tv_change_branch -> {
                    startActivity(Intent(this, BranchActivity::class.java))
                }
                R.id.tv_send_order -> {
                    present?.createOrder();
                }
                else -> {

                }

            }
        }
    }

    override fun showUserInfo(profile: ProfileModel) {
        tv_profile_name.text = profile.fullname
        Glide.with(this).asBitmap().placeholder(R.drawable.avatar_default).load(profile.avatar).into(imv_profile_avatar)
    }

    override fun showBranchInfo(branch: BranchModel) {
        branch.apply {
            tv_branch_name.text = name
            tv_branch_address.text = address
        }
    }

    override fun showCartProduct(cart: CartModel) {
        this.cart = cart
        tv_shipping_fee_price.text = NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(cart.getShippingFeeExpect())
        tv_total_price.text = NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(cart.getTotalPrice())
        cart.listProduct.let {
            if (it.isEmpty()) {
                onBackPressed()
                finish()
                return
            }
            val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            val item = CartProductItem(this, it, { cartProduct ->
                present?.plus(cartProduct)
            }, { cartProduct ->
                present?.minus(cartProduct)
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
        dialog?.apply {
            setDescription(getString(errMessage))
            setOKListener {}
            hideBtnCancel()
            show()
        }
    }

    override fun toOrderDetailScreen(ordercode: String) {
        finish()
        startActivity(OrderDetailActivity.newInstance(this, ordercode))
    }

    private fun updateTotalPrice(cart: CartModel) {
        tv_total_price.text = NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(cart.getTotalPrice())
    }

    override fun onStart() {
        super.onStart()
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: Event) {
        when (event.key) {
            Constants.EventKey.CHANGE_BRANCH -> {
                present?.let {
                    it.getBranchFromRes()
                    it.generationCart()
                    it.getCartFromRes()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }


}