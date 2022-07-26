package com.example.client.screens.promotion.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.client.R
import com.example.client.app.Constants
import com.example.client.base.BaseActivity
import com.example.client.screens.product.navigate.INavigatePromotion
import com.example.client.screens.promotion.fragment.PromotionFragment
import com.example.client.screens.promotion.navigate.NavigatorPromotion
import com.example.client.utils.ActivityUtils

class PromotionActivity : BaseActivity(), INavigatePromotion {

    companion object {
        @JvmStatic
        fun newInstance(from: Activity) = Intent(from, PromotionActivity::class.java).apply {
            putExtra(Constants.SHOW_PROMOTION_DETAIL, false)
        }

        fun newInstance(from: Activity, showPromotionDetail: Boolean) = Intent(from, PromotionActivity::class.java).apply {
            putExtra(Constants.SHOW_PROMOTION_DETAIL, showPromotionDetail)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_promotion)
        NavigatorPromotion.onStart(this)
    }

    override fun bindComponent() {
        val showPromotionDetail = intent.getBooleanExtra(Constants.SHOW_PROMOTION_DETAIL, false)
        if (showPromotionDetail) addFragment(PromotionFragment.newInstance(), PromotionFragment::class.simpleName
                ?: "")
        else addFragment(PromotionFragment.newInstance(), PromotionFragment::class.simpleName ?: "")
    }

    override fun onBackPressed() {
        supportFragmentManager.run {
            if (fragments.size == 1 || backStackEntryCount == 1) {
                finish()
            } else {
                ActivityUtils.popFragment(this)
            }
        }
    }

    override fun replaceFragment(fragment: Fragment?, TAG: String) {
        fragment ?: return
        ActivityUtils.replaceFragmentInActivity(supportFragmentManager, fragment, R.id.container, TAG)
    }

    override fun addFragment(fragment: Fragment?, TAG: String) {
        fragment ?: return
        ActivityUtils.addFragmentToActivity(supportFragmentManager, fragment, R.id.container, TAG)
    }

    override fun popFragment() {
        ActivityUtils.popFragment(supportFragmentManager)
    }
}