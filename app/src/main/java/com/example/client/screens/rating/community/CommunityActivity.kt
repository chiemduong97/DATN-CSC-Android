package com.example.client.screens.rating.community

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.client.R
import com.example.client.app.Constants
import com.example.client.base.BaseActivity
import com.example.client.models.rating.RatingModel
import com.example.client.screens.rating.community.fragment.CommunityFragment
import com.example.client.screens.rating.community.fragment.RatingDetailFragment
import com.example.client.screens.rating.navigate.INavigateRating
import com.example.client.screens.rating.navigate.NavigatorRating
import com.example.client.utils.ActivityUtils

class CommunityActivity : BaseActivity(), INavigateRating {

    companion object {
        @JvmStatic
        fun newInstance(from: Activity): Intent = Intent(from, CommunityActivity::class.java).apply {
            putExtra(Constants.SHOW_RATING_DETAIL, false)
        }

        @JvmStatic
        fun newInstance(from: Activity, rating: RatingModel, showRatingDetail: Boolean) = Intent(from, CommunityActivity::class.java).apply {
            putExtra(Constants.BundleKey.RATING_MODEL, rating)
            putExtra(Constants.SHOW_RATING_DETAIL, showRatingDetail)
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)
        NavigatorRating.onStart(this)
    }

    override fun bindComponent() {
        val showRatingDetail = intent.getBooleanExtra(Constants.SHOW_RATING_DETAIL, false)
        if (showRatingDetail) addFragment(RatingDetailFragment.newInstance(intent?.extras
                ?: Bundle()), RatingDetailFragment::class.java.simpleName)
        else addFragment(CommunityFragment.newInstance(intent?.extras
                ?: Bundle()), CommunityFragment::class.java.simpleName)
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

    override fun replaceFragment(fragment: Fragment?, TAG: String) {
        fragment ?: return
        ActivityUtils.replaceFragmentInActivity(supportFragmentManager, fragment, R.id.container, TAG)
    }

    override fun addFragment(fragment: Fragment?, TAG: String) {
        fragment ?: return
        ActivityUtils.addFragmentToActivity(supportFragmentManager, fragment, R.id.container, TAG)
    }

    override fun popFragment() {
        ActivityUtils.popFragment(supportFragmentManager)
    }
}