package com.example.client.screens.cart.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.client.R
import com.example.client.models.branch.BranchModel
import com.example.client.models.cart.CartModel
import com.example.client.models.profile.ProfileModel
import com.example.client.screens.branch.BranchActivity
import com.example.client.screens.cart.item.CartProductItem
import com.example.client.screens.cart.present.CartPresent
import kotlinx.android.synthetic.main.activity_cart.*

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
        present = CartPresent(this)
        present?.let {
            it.getBranchFromRes()
            it.getCartFromRes()
            it.getUserFromRes()
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
    }

    override fun showCartProduct(cart: CartModel) {
        cart.listProduct?.let {
            if (it.isEmpty()) {
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


    }

}