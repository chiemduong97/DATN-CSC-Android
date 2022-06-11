package com.example.client.screens.product.navigate

import android.os.Bundle
import com.example.client.screens.product.fragment.ProductFragment

object Navigator {

    private var mView: INavigateProduct? = null

    @JvmStatic
    fun onStart(view: INavigateProduct?) {
        this.mView = view
    }

    @JvmStatic
    fun showProductScreen(args: Bundle?) {
        mView?.addFragment(ProductFragment.newInstance(args), ProductFragment::class.java.simpleName)
    }


}