package com.example.client.screens.rating.community.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.client.R
import com.example.client.app.Constants
import com.example.client.base.BaseCollectionFragment
import com.example.client.models.rating.RatingModel
import com.example.client.screens.rating.community.item.RatingItem
import com.example.client.screens.rating.community.present.CommunityPresent
import com.example.client.screens.rating.community.present.ICommunityPresent
import com.example.client.screens.rating.navigate.NavigatorRating
import kotlinx.android.synthetic.main.fragment_community.*

class CommunityFragment : BaseCollectionFragment<ICommunityPresent>(), ICommunityView {
    private var mItems = arrayListOf<RatingModel>()
    private val manager by lazy { LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false) }

    companion object {
        @JvmStatic
        fun newInstance(args: Bundle?) = CommunityFragment().apply {
            arguments = args
        }
    }

    override val presenter: ICommunityPresent
        get() = CommunityPresent(this)

    override fun bindData() {
        mPresenter?.bindData()
    }

    override fun bindEvent() {
        imv_back.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    override fun showData(items: List<RatingModel>) {
        mItems = items as ArrayList<RatingModel>
        recycler_view.visibility = View.VISIBLE
        imv_empty.visibility = View.GONE
        val item = RatingItem(requireContext(), mItems) { rating ->
            NavigatorRating.showRatingDetailScreen(arguments?.apply {
                putSerializable(Constants.BundleKey.RATING_MODEL, rating)
            })
        }
        recycler_view.layoutManager = manager
        recycler_view.adapter = item
    }

    override fun showMoreData(items: List<RatingModel>) {
        mItems.addAll(items)
        recycler_view.adapter?.notifyDataSetChanged()
    }

    override fun showEmptyData() {
        recycler_view.visibility = View.GONE
        imv_empty.visibility = View.VISIBLE
    }

    override fun showErrorMessage(errMessage: Int) {
        showToastMessage(getString(errMessage))
    }

    override fun navigateToRatingDetailScreen(rating: RatingModel) {

    }


    override fun getLayout(): Int {
        return R.layout.fragment_community
    }

    override fun onBackPress() {
        requireActivity().onBackPressed()
    }

    override fun shouldLoadMore() = true
}