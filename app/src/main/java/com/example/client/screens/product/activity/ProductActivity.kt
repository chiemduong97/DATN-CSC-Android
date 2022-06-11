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
import com.example.client.screens.product.fragment.ProductFragment
import com.example.client.screens.product.navigate.INavigateProduct
import com.example.client.screens.product.navigate.Navigator
import com.example.client.utils.ActivityUtils


class ProductActivity : BaseActivity(), INavigateProduct {

    companion object {
        @JvmStatic
        fun newInstance(from: Activity,categoryModel: CategoryModel): Intent = Intent(from, ProductActivity::class.java).apply {
            putExtra(Constants.CATEGORY_MODEL, categoryModel)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        Navigator.onStart(this)
    }

    override fun bindComponent() {
        addFragment(ProductFragment.newInstance(intent?.extras?:Bundle()), ProductFragment::class.simpleName ?: "")
    }

    override fun onBackPressed() {
        supportFragmentManager.run {
            if (fragments.size == 1 ||  backStackEntryCount == 1) {
                finish()
            } else {
                super.onBackPressed()
            }
        }
    }

    override fun replaceFragment(fragment: Fragment?, TAG: String) {
        fragment ?: return
        ActivityUtils.replaceFragmentInActivity(supportFragmentManager, fragment, R.id.container)
    }

    override fun addFragment(fragment: Fragment?, TAG: String) {
        fragment ?: return
        ActivityUtils.addFragmentToActivity(supportFragmentManager, fragment, R.id.container)
    }

    override fun popFragment() {
        ActivityUtils.popFragment(supportFragmentManager)
    }

}