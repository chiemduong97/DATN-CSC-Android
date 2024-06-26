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
import com.example.client.dialog.AddToCartDialog
import com.example.client.dialog.OptionAddToCartListener
import com.example.client.models.cart.CartProductModel
import com.example.client.models.category.CategoryModel
import com.example.client.models.product.ProductModel
import com.example.client.screens.cart.activity.CartActivity
import com.example.client.screens.category.activity.CategoryActivity
import com.example.client.screens.category.parent.item.ProductsItem
import com.example.client.screens.category.parent.item.SuperCategoryItem
import com.example.client.screens.category.parent.present.ISuperCategoryPresent
import com.example.client.screens.category.parent.present.SuperCategoryPresent
import com.example.client.screens.product.activity.ProductActivity
import com.example.client.screens.search.activity.SearchActivity
import kotlinx.android.synthetic.main.activity_super_category.*

class SuperCategoryActivity : BaseActivityMVP<ISuperCategoryPresent>(), ISuperCategoryView, View.OnClickListener, OptionAddToCartListener {

    companion object {
        @JvmStatic
        fun newInstance(from: Activity, categoryModel: CategoryModel) = Intent(from, SuperCategoryActivity::class.java).apply {
            putExtra(Constants.BundleKey.CATEGORY_MODEL, categoryModel)
        }
    }

    private var mCategoryModel: CategoryModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_super_category)
        mCategoryModel = intent.getSerializableExtra(Constants.BundleKey.CATEGORY_MODEL) as CategoryModel
        mCategoryModel?.id?.let { presenter.getCategories(it) }
    }

    override fun bindData() {
        presenter.run {
            getSuperCategories()
            getCartFromRes()
        }

    }

    override fun bindEvent() {
        imv_back.setOnClickListener(this)
        imv_more.setOnClickListener(this)
        cv_cart_place.setOnClickListener(this)
        tv_search.setOnClickListener(this)
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
            presenter.onClickSuperCategory(categoryModel)
        }
        recycler_view_super_category.adapter = item
    }

    override fun showCategories(items: List<CategoryModel>) {
        imv_empty.visibility = View.GONE
        recycler_view.visibility = View.VISIBLE
        val manager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recycler_view.layoutManager = manager
        val item = ProductsItem(
                this, items,
                {
                    startActivity(ProductActivity.newInstance(this, it))
                },
                { prod ->
                    startActivity(ProductActivity.newInstance(this, prod, true))
                },
                { prod ->
                    showAddToCartDialog(prod)
                }
        )
        recycler_view.adapter = item
    }

    override fun showCart(quantity: Int) {
        rll_cart.visibility = View.VISIBLE
        tv_cart_quantity.text = quantity.toString()
    }

    override fun hideCart() {
        rll_cart.visibility = View.GONE
    }

    override fun showEmptyData() {
        imv_empty.visibility = View.VISIBLE
        recycler_view.visibility = View.GONE
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
        when (v.id) {
            R.id.imv_back -> {
                onBackPressed()
            }
            R.id.imv_more -> {
                startActivity(CategoryActivity.newInstance(this))
            }
            R.id.cv_cart_place -> {
                startActivity(Intent(CartActivity.newInstance(this)))
            }
            R.id.tv_search -> {
                startActivity(SearchActivity.newInstance(this))
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        mCategoryModel = intent?.getSerializableExtra(Constants.BundleKey.CATEGORY_MODEL) as CategoryModel
        presenter.run {
            getSuperCategories()
            mCategoryModel?.id?.let { getCategories(it) }
            getCartFromRes()
        }
    }

    private fun showAddToCartDialog(product: ProductModel) {
        val dialog = AddToCartDialog.newInstance(product)
        dialog.setListener(this)
        dialog.show(supportFragmentManager)
    }

    override fun addToCart(cartProduct: CartProductModel) {
        presenter.run {
            addToCart(cartProduct)
            getCartFromRes()
        }
    }

    override fun showProductDetail(product: ProductModel) {
        startActivity(ProductActivity.newInstance(this, product, true))
    }

}