package com.example.client.screens.notify

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.example.client.R
import com.example.client.base.BaseActivity
import com.example.client.utils.ActivityUtils

class NotifyActivity : BaseActivity(){

    companion object {
        @JvmStatic
        fun newInstance(from: Activity): Intent = Intent(from, NotifyActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notify)
    }

    override fun bindComponent() {
        ActivityUtils.addFragmentToActivity(supportFragmentManager, NotifyFragment.newInstance(), R.id.container, NotifyFragment::class.simpleName)
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