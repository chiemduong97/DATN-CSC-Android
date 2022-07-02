package com.example.client.screens.profile.manager_info.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.client.R
import com.example.client.base.BaseActivity
import com.example.client.screens.profile.manager_info.fragment.ManagerProfileFragment
import com.example.client.screens.profile.navigate.INavigateProfile
import com.example.client.screens.profile.navigate.NavigatorProfile
import com.example.client.utils.ActivityUtils

class ManagerProfileActivity : BaseActivity(), INavigateProfile {

    companion object {
        @JvmStatic
        fun newInstance(from: Activity): Intent = Intent(from, ManagerProfileActivity::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager_profile)
        NavigatorProfile.onStart(this)
    }

    override fun bindComponent() {
        addFragment(ManagerProfileFragment.newInstance(), ManagerProfileFragment::class.simpleName ?: "")
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

    override fun replaceFragment(fragment: Fragment?, TAG: String) {
        fragment ?: return
        ActivityUtils.replaceFragmentInActivity(supportFragmentManager, fragment, R.id.container, TAG)
    }

    override fun addFragment(fragment: Fragment?, TAG: String) {
        fragment ?: return
        ActivityUtils.addFragmentToActivity(supportFragmentManager, fragment, R.id.container, TAG)
    }

    override fun popFragment() {
        onBackPressed()
    }

}