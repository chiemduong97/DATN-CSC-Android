package com.example.client.screens.cart.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.client.R
import com.example.client.app.Constants
import com.example.client.models.branch.BranchModel
import com.example.client.models.cart.CartModel
import com.example.client.models.event.Event
import com.example.client.models.profile.ProfileModel
import com.example.client.screens.branch.BranchActivity
import com.example.client.screens.cart.item.CartProductItem
import com.example.client.screens.cart.present.CartPresent
import com.example.client.screens.order.activity.ReviewOrderActivity
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_cart.imv_back
import kotlinx.android.synthetic.main.activity_cart.imv_avatar
import kotlinx.android.synthetic.main.activity_cart.recyclerView
import kotlinx.android.synthetic.main.activity_product_detail.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.NumberFormat
import java.util.*

class CartActivity : AppCompatActivity(), ICartView, View.OnClickListener {
    private var present: CartPresent? = null
    companion object{
        fun newInstance(context: Context?) : Intent{
            return Intent(context, CartActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        rll_change_branch.setOnClickListener(this)
        imv_back.setOnClickListener(this)
        tv_order.setOnClickListener(this)
        present = CartPresent(this)
        present?.let {
            it.getBranchFromRes()
            it.getCartFromRes()
            it.getUserFromRes()
            it.generationCart()
        }
    }

    override fun onClick(v: View?) {
        v?.let {
            when (it.id) {
                R.id.rll_change_branch -> {
                    startActivity(Intent(this, BranchActivity::class.java))
                }
                R.id.imv_back -> {
                    onBackPressed()
                    finish()
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
        Glide.with(this).asBitmap().placeholder(R.drawable.avatar_default).load(profile.avatar).into(imv_avatar)
    }

    override fun showCartProduct(cart: CartModel) {
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

    override fun updateTotalPrice(cart: CartModel) {
        tv_total_price.text = NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(cart.getAmount())
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
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}