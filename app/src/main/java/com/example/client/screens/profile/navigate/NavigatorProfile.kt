package com.example.client.screens.profile.navigate

import android.content.Context
import com.example.client.screens.login.activity.LoginEmailActivity

object NavigatorProfile {

    private var mView: INavigateProfile? = null

    @JvmStatic
    fun onStart(view: INavigateProfile?) {
        this.mView = view
    }
//
//    @JvmStatic
//    fun showLoginEmailScreen(context: Context) {
//        mView?.addFragment(LoginEmailActivity.newInstance(context), LoginEmailActivity::class.java.simpleName)
//    }


}