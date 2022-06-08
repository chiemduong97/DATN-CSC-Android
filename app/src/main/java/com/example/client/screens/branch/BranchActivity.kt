package com.example.client.screens.branch

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.example.client.R
import com.example.client.base.BaseActivity
import com.example.client.utils.ActivityUtils

class BranchActivity : BaseActivity() {

    companion object {
        @JvmStatic
        fun newInstance(from: Activity): Intent = Intent(from, BranchActivity::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_branch)
    }

    override fun bindComponent() {
        ActivityUtils.addFragmentToActivity(supportFragmentManager, BranchFragment.newInstance(), R.id.container, BranchFragment::class.java.simpleName)
    }

    override fun onBackPressed() {
        supportFragmentManager.run {
            if (fragments.size == 1 ||  backStackEntryCount == 1) {
                finish()
            } else {
                super.onBackPressed()
            }
        }
    }
}