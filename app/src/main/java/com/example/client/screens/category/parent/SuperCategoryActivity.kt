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
import kotlinx.android.synthetic.main.activity_super_category.cv_cart_place
import kotlinx.android.synthetic.main.activity_super_category.imv_back
import kotlinx.android.synthetic.main.activity_super_category.recycler_view
import kotlinx.android.synthetic.main.activity_super_category.rll_cart
import kotlinx.android.synthetic.main.activity_super_category.rll_loading
import kotlinx.android.synthetic.main.activity_super_category.tv_cart_quantity
import kotlinx.android.synthetic.main.fragment_product_detail.*

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
    }

    override fun bindData() {
        presenter.bindData()
    }

    override fun bindEvent() {
        imv_back.setOnClickListener(this)
        imv_more.setOnClickListener(this)
        cv_cart_place.setOnClickListener(this)
        search_view.setOnQueryTextFocusChangeListener { _: View, hasFocus: Boolean ->
            if (hasFocus) {
                startActivity(SearchActivity.newInstance(this))
                search_view.clearFocus()
            }
        }
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

    override fun showProducts(items: List<CategoryModel>) {
        val manager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recycler_view.layoutManager = manager
        val item = ProductsItem(
                this, items,
                {
                    startActivity(ProductActivity.newInstance(this, it))
                },
                { cate: CategoryModel, prod: ProductModel ->
                    startActivity(ProductActivity.newInstance(this, cate, prod, true))
                },
                { cate: CategoryModel, prod: ProductModel ->
                    showAddToCartDialog(cate, prod)
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
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        mCategoryModel = intent?.getSerializableExtra(Constants.BundleKey.CATEGORY_MODEL) as CategoryModel
        presenter.bindData()
    }

    private fun showAddToCartDialog(category: CategoryModel, product: ProductModel) {
        val dialog = AddToCartDialog.newInstance(category, product)
        dialog.setListener(this)
        dialog.show(supportFragmentManager)
    }

    override fun addToCart(cartProduct: CartProductModel) {
        presenter.run {
            addToCart(cartProduct)
            getCartFromRes()
        }
    }

    override fun showProductDetail(category: CategoryModel, product: ProductModel) {
        startActivity(ProductActivity.newInstance(this, category, product, true))
    }

}