package com.example.client.screens.product.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.client.R
import com.example.client.app.Constants
import com.example.client.base.BaseActivity
import com.example.client.models.category.CategoryModel
import com.example.client.models.product.ProductModel
import com.example.client.screens.product.detail.ProductDetailFragment
import com.example.client.screens.product.fragment.ProductFragment
import com.example.client.screens.product.navigate.INavigateProduct
import com.example.client.screens.product.navigate.NavigatorProduct
import com.example.client.utils.ActivityUtils


class ProductActivity : BaseActivity(), INavigateProduct {

    companion object {
        @JvmStatic
        fun newInstance(from: Activity, categoryModel: CategoryModel): Intent = Intent(from, ProductActivity::class.java).apply {
            putExtra(Constants.BundleKey.CATEGORY_MODEL, categoryModel)
            putExtra(Constants.SHOW_PRODUCT_DETAIL, false)
        }

        @JvmStatic
        fun newInstance(from: Activity, productModel: ProductModel, showProductDetail: Boolean) = Intent(from, ProductActivity::class.java).apply {
            putExtra(Constants.BundleKey.PRODUCT_MODEL, productModel)
            putExtra(Constants.SHOW_PRODUCT_DETAIL, showProductDetail)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        NavigatorProduct.onStart(this)
    }

    override fun bindComponent() {
        val showProductDetail = intent.getBooleanExtra(Constants.SHOW_PRODUCT_DETAIL, false)
        if (showProductDetail) addFragment(ProductDetailFragment.newInstance(intent?.extras
                ?: Bundle()), ProductDetailFragment::class.java.simpleName)
        else addFragment(ProductFragment.newInstance(intent?.extras
                ?: Bundle()), ProductFragment::class.java.simpleName)
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