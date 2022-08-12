package com.example.client.screens.rating.navigate

import android.os.Bundle
import com.example.client.screens.rating.community.fragment.CommunityFragment
import com.example.client.screens.rating.community.fragment.RatingDetailFragment

object NavigatorRating {

    private var mView: INavigateRating? = null

    @JvmStatic
    fun onStart(view: INavigateRating?) {
        this.mView = view
    }

    @JvmStatic
    fun popFragment() {
        mView?.popFragment()
    }

    @JvmStatic
    fun showRatingScreen(args: Bundle?) {
        mView?.addFragment(CommunityFragment.newInstance(args), CommunityFragment::class.java.simpleName)
    }

    @JvmStatic
    fun showRatingDetailScreen(args: Bundle?) {
        mView?.addFragment(RatingDetailFragment.newInstance(args), RatingDetailFragment::class.java.simpleName)
    }

}