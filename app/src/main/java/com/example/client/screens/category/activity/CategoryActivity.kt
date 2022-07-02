package com.example.client.screens.category.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.client.R
import com.example.client.base.BaseActivityMVP
import com.example.client.models.category.CategoryModel
import com.example.client.screens.category.parent.SuperCategoryActivity
import com.example.client.screens.category.present.CategoryPresent
import com.example.client.screens.category.present.ICategoryPresent
import com.example.client.screens.category.item.HomeCategoryItem
import com.example.client.screens.product.activity.ProductActivity
import kotlinx.android.synthetic.main.activity_category.*

class CategoryActivity : BaseActivityMVP<ICategoryPresent>(), ICategoryView, SwipeRefreshLayout.OnRefreshListener {

    companion object {
        @JvmStatic
        fun newInstance(from: Activity): Intent = Intent(from, CategoryActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
    }

    override fun bindEvent() {
        imv_back.setOnClickListener{
            onBackPressed()
        }
        swipe_refresh.setOnRefreshListener(this)
    }

    override fun bindData() {
        presenter.getCategories()
    }

    override val presenter: ICategoryPresent
        get() = CategoryPresent(this)

    override fun showCategories(items: List<CategoryModel>) {
        imv_empty.visibility = View.GONE
        recycler_view.visibility = View.VISIBLE
        val manager = GridLayoutManager(this, 4, RecyclerView.VERTICAL, false)
        recycler_view.layoutManager = manager
        val item = HomeCategoryItem(this, items, { categoryModel ->
            startActivity(SuperCategoryActivity.newInstance(this, categoryModel))
        }, {})
        recycler_view.adapter = item
    }

    override fun showEmptyData() {
        imv_empty.visibility = View.VISIBLE
        recycler_view.visibility = View.GONE
    }

    override fun showErrorMessage(errorMessage: Int) {
        showToastMessage(getString(errorMessage))
    }

    override fun showLoading() {
        swipe_refresh.isRefreshing = true
    }

    override fun hideLoading() {
        swipe_refresh.run {
            if (isRefreshing) isRefreshing = false
        }
    }

    override fun onBackPress() {}
    override fun onRefresh() {
        presenter.getCategories()
    }
}