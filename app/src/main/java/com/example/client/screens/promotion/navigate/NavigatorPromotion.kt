package com.example.client.screens.promotion.navigate

import android.os.Bundle
import com.example.client.screens.product.navigate.INavigatePromotion
import com.example.client.screens.promotion.fragment.PromotionDetailFragment

object NavigatorPromotion {

    private var mView: INavigatePromotion? = null

    @JvmStatic
    fun onStart(view: INavigatePromotion?) {
        this.mView = view
    }

    @JvmStatic
    fun popFragment() {
        mView?.popFragment()
    }

    @JvmStatic
    fun showPromotionDetailScreen(args: Bundle?) {
        mView?.addFragment(PromotionDetailFragment.newInstance(args), PromotionDetailFragment::class.java.simpleName)
    }

}