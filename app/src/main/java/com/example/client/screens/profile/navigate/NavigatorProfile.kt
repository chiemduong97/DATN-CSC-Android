package com.example.client.screens.profile.navigate

import com.example.client.screens.profile.manager_info.fragment.UpdateInfoFragment
import com.example.client.screens.profile.manager_info.fragment.UpdatePasswordFragment

object NavigatorProfile {

    private var mView: INavigateProfile? = null

    @JvmStatic
    fun onStart(view: INavigateProfile?) {
        this.mView = view
    }

    @JvmStatic
    fun popFragment() {
        mView?.popFragment()
    }

    @JvmStatic
    fun navigateToUpdateInfoScreen() {
        mView?.addFragment(UpdateInfoFragment.newInstance(), UpdateInfoFragment::class.java.simpleName)
    }

    fun navigateToUpdatePasswordScreen() {
        mView?.addFragment(UpdatePasswordFragment.newInstance(), UpdatePasswordFragment::class.java.simpleName)
    }
}