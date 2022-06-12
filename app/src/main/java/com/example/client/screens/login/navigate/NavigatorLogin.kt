package com.example.client.screens.login.navigate

import android.os.Bundle
import com.example.client.screens.product.fragment.ProductFragment

object NavigatorLogin {

    private var mView: INavigateLogin? = null

    @JvmStatic
    fun onStart(view: INavigateLogin?) {
        this.mView = view
    }

    @JvmStatic
    fun showProductScreen(args: Bundle?) {
        mView?.addFragment(ProductFragment.newInstance(args), ProductFragment::class.java.simpleName)
    }


}