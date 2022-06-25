package com.example.client.screens.product.navigate

import android.os.Bundle
import com.example.client.screens.product.detail.ProductDetailFragment
import com.example.client.screens.product.fragment.ProductFragment

object NavigatorProduct {

    private var mView: INavigateProduct? = null

    @JvmStatic
    fun onStart(view: INavigateProduct?) {
        this.mView = view
    }

    @JvmStatic
    fun popFragment() {
        mView?.popFragment()
    }

    @JvmStatic
    fun showProductScreen(args: Bundle?) {
        mView?.addFragment(ProductFragment.newInstance(args), ProductFragment::class.java.simpleName)
    }

    @JvmStatic
    fun showProductDetailScreen(args: Bundle?) {
        mView?.addFragment(ProductDetailFragment.newInstance(args), ProductDetailFragment::class.java.simpleName)
    }
}