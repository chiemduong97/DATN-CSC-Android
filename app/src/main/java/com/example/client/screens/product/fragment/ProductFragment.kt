package com.example.client.screens.product.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.client.R
import com.example.client.app.Constants
import com.example.client.base.BaseCollectionFragment
import com.example.client.models.cart.CartModel
import com.example.client.models.category.CategoryModel
import com.example.client.models.product.ProductModel
import com.example.client.screens.product.activity.IProductView
import com.example.client.screens.product.item.ProductVerticalItem
import com.example.client.screens.product.present.IProductPresent
import com.example.client.screens.product.present.ProductPresent
import kotlinx.android.synthetic.main.fragment_product.*

class ProductFragment: BaseCollectionFragment<IProductPresent>(), IProductView, View.OnClickListener {
    private var mItems = arrayListOf<ProductModel>()
    private val manager by lazy { LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false) }

    companion object {
        @JvmStatic
        fun newInstance(args: Bundle?) = ProductFragment().apply {
            arguments = args
        }
    }

    private val categoryModel by lazy { arguments?.getSerializable(Constants.CATEGORY_MODEL) as CategoryModel }
    override val presenter: ProductPresent
        get() = ProductPresent(this)

    override fun onRefresh() {
        presenter.binData(categoryModel)
    }

    override fun bindData() {
        presenter.binData(categoryModel)
    }

    override fun bindComponent() {
        tv_title.text = categoryModel.name
    }

    override fun bindEvents() {
        imv_back.setOnClickListener(this)
    }

    override fun showData(items: List<ProductModel>, cart: CartModel) {
        mItems = items as ArrayList<ProductModel>
        recycler_view.visibility = View.VISIBLE
        imv_empty.visibility = View.GONE
        val item = context?.let { it ->
            ProductVerticalItem(it, mItems, cart.listProduct) { product ->
                presenter.onClickItem(product)
            }
        }
        recycler_view.layoutManager = manager
        recycler_view.adapter = item
    }

    override fun showMoreData(items: List<ProductModel>, cart: CartModel) {
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

    override fun onClick(v: View?) {
        if (v?.id == R.id.imv_back) {
            activity?.onBackPressed()
        }
    }

    override fun getLayout(): Int {
        return R.layout.fragment_product
    }

    override fun onBackPress() {
        activity?.onBackPressed()
    }

    override fun shouldLoadMore() = true
}