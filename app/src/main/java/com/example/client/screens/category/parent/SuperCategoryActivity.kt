package com.example.client.screens.category.parent

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.client.R
import com.example.client.app.Constants
import com.example.client.base.BaseActivityMVP
import com.example.client.models.category.CategoryModel
import com.example.client.models.product.ProductModel
import com.example.client.screens.category.activity.CategoryActivity
import com.example.client.screens.category.parent.item.ProductsItem
import com.example.client.screens.category.parent.item.SuperCategoryItem
import com.example.client.screens.category.parent.present.ISuperCategoryPresent
import com.example.client.screens.category.parent.present.SuperCategoryPresent
import com.example.client.screens.product.activity.ProductActivity
import kotlinx.android.synthetic.main.activity_super_category.*

class SuperCategoryActivity : BaseActivityMVP<ISuperCategoryPresent>(), ISuperCategoryView, View.OnClickListener {

    companion object {
        @JvmStatic
        fun newInstance(from: Activity,categoryModel: CategoryModel) = Intent(from, SuperCategoryActivity::class.java).apply {
            putExtra(Constants.CATEGORY_MODEL, categoryModel)
        }
    }

    private var mCategoryModel: CategoryModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_super_category)
        mCategoryModel  = intent.getSerializableExtra(Constants.CATEGORY_MODEL) as CategoryModel
    }

    override fun bindData() {
        presenter.bindData()
    }

    override fun bindEvent() {
        imv_back.setOnClickListener(this)
        imv_more.setOnClickListener(this)
    }

    override val presenter: ISuperCategoryPresent
        get() = SuperCategoryPresent(this)

    override fun showErrorMessage(errMessage: Int) {
        showToastMessage(getString(errMessage))
    }

    override fun showSuperCategories(items: List<CategoryModel>) {
        val manager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        recycler_view_super_category.layoutManager = manager
        recycler_view_super_category.scrollToPosition(items.indexOfFirst { it.id == mCategoryModel?.id })
        val item = SuperCategoryItem(this, items.onEach { category ->
            if (category.id == mCategoryModel?.id) category.selected = true
        }) { categoryModel ->
            val index = items.indexOfFirst { it.id == mCategoryModel?.id }
            (items as MutableList<CategoryModel>)[index] = mCategoryModel!!.apply {
                selected = false
            }
            recycler_view_super_category.adapter?.notifyItemChanged(index)
            mCategoryModel = categoryModel
            recycler_view_super_category.scrollToPosition(items.indexOfFirst { it.id == categoryModel.id })
            presenter.onClickItem(categoryModel)
        }
        recycler_view_super_category.adapter = item
    }

    override fun showProducts(items: List<CategoryModel>) {
        val manager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recycler_view.layoutManager = manager
        val item = ProductsItem(this, items, {
            startActivity(ProductActivity.newInstance(this, it))
        }) { cate: CategoryModel, prod: ProductModel ->
            startActivity(ProductActivity.newInstance(this, cate, prod, true))
        }
        recycler_view.adapter = item
    }

    override fun showLoading() {
        rll_loading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        rll_loading.visibility = View.GONE
    }

    override fun onBackPress() {
        super.onBackPressed()
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.imv_back -> {
                onBackPressed()
            }
            R.id.imv_more -> {
                startActivity(CategoryActivity.newInstance(this))
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        mCategoryModel  = intent?.getSerializableExtra(Constants.CATEGORY_MODEL) as CategoryModel
        presenter.bindData()
    }

}