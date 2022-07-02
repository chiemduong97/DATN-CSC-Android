package com.example.client.screens.order.history.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.example.client.R
import com.example.client.base.BaseActivity
import com.example.client.screens.order.history.OrderHistoryFragment
import com.example.client.utils.ActivityUtils

class OrderHistoryActivity : BaseActivity(){

    companion object {
        @JvmStatic
        fun newInstance(from: Activity): Intent = Intent(from, OrderHistoryActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history)
    }

    override fun bindComponent() {
        ActivityUtils.addFragmentToActivity(supportFragmentManager, OrderHistoryFragment.newInstance(), R.id.container, OrderHistoryFragment::class.simpleName)
    }

    override fun onBackPressed() {
        supportFragmentManager.run {
            if (fragments.size == 1 ||  backStackEntryCount == 1) {
                finish()
            } else {
                ActivityUtils.popFragment(this)
            }
        }
    }
}