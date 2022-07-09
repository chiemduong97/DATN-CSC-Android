package com.example.client.screens.search.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.example.client.R
import com.example.client.app.Constants
import com.example.client.base.BaseActivity
import com.example.client.screens.product.detail.ProductDetailFragment
import com.example.client.screens.product.fragment.ProductFragment
import com.example.client.screens.search.fragment.SearchFragment
import com.example.client.utils.ActivityUtils

class SearchActivity : BaseActivity() {

    companion object {
        @JvmStatic
        fun newInstance(from: Activity): Intent = Intent(from, SearchActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
    }

    override fun bindComponent() {
        ActivityUtils.addFragmentToActivity(
                supportFragmentManager,
                SearchFragment.newInstance(),
                R.id.container,
                ProductFragment::class.simpleName
        )
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
}