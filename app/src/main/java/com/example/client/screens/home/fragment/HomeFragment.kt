package com.example.client.screens.home.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.bumptech.glide.Glide
import com.example.client.R
import com.example.client.base.BaseFragmentMVP
import com.example.client.models.branch.BranchModel
import com.example.client.models.category.CategoryModel
import com.example.client.models.profile.ProfileModel
import com.example.client.screens.branch.BranchActivity
import com.example.client.screens.category.activity.CategoryActivity
import com.example.client.screens.category.parent.SuperCategoryActivity
import com.example.client.screens.category.item.HomeCategoryItem
import com.example.client.screens.home.present.HomePresent
import com.example.client.screens.home.present.IHomePresent
import com.example.client.screens.map.activity.MapsActivity
import com.example.client.screens.profile.manager_info.activity.ManagerProfileActivity
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : BaseFragmentMVP<IHomePresent>(), View.OnClickListener, IHomeView, OnRefreshListener {

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, null)
    }

    override fun bindData() {
        presenter.binData()
        presenter.getCategories()
    }

    override fun bindEvent() {
        swipe_refresh.setOnRefreshListener(this)
        lnl_profile.setOnClickListener(this)
        rll_change_branch.setOnClickListener(this)
        lnl_order_location.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.lnl_profile -> startActivity(ManagerProfileActivity.newInstance(requireActivity()))
            R.id.rll_change_branch -> startActivity(BranchActivity.newInstance(requireActivity()))
            R.id.lnl_order_location -> startActivity(MapsActivity.newInstance(requireActivity()))
        }
    }

    override fun showCategories(items: List<CategoryModel>) {
        recycler_view_category.visibility = View.VISIBLE
        val manager = GridLayoutManager(context, 4, GridLayoutManager.VERTICAL, false)
        recycler_view_category.layoutManager = manager
        val item = HomeCategoryItem(requireContext(), items, { categoryModel ->
            startActivity(SuperCategoryActivity.newInstance(requireActivity(), categoryModel))
        }, {
            startActivity(CategoryActivity.newInstance(requireActivity()))
        })
        recycler_view_category.adapter = item
    }

    override fun hideCategories() {
        recycler_view_category.visibility = View.GONE
    }

//    override fun showBanners(items: List<BannerModel>) {
//        val item = activity?.let { BannerItem(it, items) }
//        view_pager_banner.adapter = item
//        TabLayoutMediator(tab_dot, view_pager_banner) { _: TabLayout.Tab?, _: Int -> }.attach()
//    }
//
//    override fun hideBanner() {
//        view_pager_banner.visibility = View.GONE
//        tab_dot.visibility = View.GONE
//    }

    override fun showBranch(branch: BranchModel) {
        tv_branch_name.text = branch.name
        tv_branch_address.text = branch.address
    }

    override fun showProfile(profile: ProfileModel) {
        profile.let {
            val format: DateFormat = SimpleDateFormat("HH", Locale.getDefault())
            val hour = format.format(Calendar.getInstance().time).toInt()
            val greet = if (hour in 4..10) getString(R.string.greet_morning) else if (hour in 11..12) getString(R.string.greet_lunch) else if (hour in 13..17) getString(R.string.greet_evening) else getString(R.string.greet_night)
            tv_greet.text = getString(R.string.greet_description, greet, it.fullname)
            Glide.with(this)
                    .asBitmap()
                    .placeholder(R.drawable.avatar_default)
                    .load(it.avatar)
                    .into(view_icon)
            tv_order_address.text = it.address
        }
    }

    override fun toBranchScreen() {
        startActivity(Intent(activity, BranchActivity::class.java))
    }

    override fun showErrorMessage(errMessage: Int) {
        showToastMessage(getString(errMessage))
    }

    override fun showLoading() {
        swipe_refresh.isRefreshing = true
    }

    override fun hideLoading() {
        swipe_refresh.run {
            if (isRefreshing) isRefreshing = false
        }
    }

    override val presenter: IHomePresent
        get() = HomePresent(this)

    override fun onBackPress() {
        requireActivity().onBackPressed()
    }

    override fun onRefresh() {
        bindData()
    }
}