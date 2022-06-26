package com.example.client.screens.product.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.client.R
import com.example.client.app.Constants
import com.example.client.base.BaseCollectionFragment
import com.example.client.models.category.CategoryModel
import com.example.client.models.event.Event
import com.example.client.models.event.ValueEvent
import com.example.client.models.product.ProductModel
import com.example.client.screens.product.activity.IProductView
import com.example.client.screens.product.item.ProductVerticalItem
import com.example.client.screens.product.navigate.NavigatorProduct
import com.example.client.screens.product.present.IProductPresent
import com.example.client.screens.product.present.ProductPresent
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_product.*

class ProductFragment : BaseCollectionFragment<IProductPresent>(), IProductView, View.OnClickListener {
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
        super.onRefresh()
        presenter.binData(categoryModel)
    }

    override fun bindData() {
        presenter.binData(categoryModel)
    }

    override fun bindComponent() {
        tv_title.text = categoryModel.name
    }

    override fun bindEvent() {
        imv_back.setOnClickListener(this)
    }

    override fun showData(items: List<ProductModel>) {
        mItems = items as ArrayList<ProductModel>
        recycler_view.visibility = View.VISIBLE
        imv_empty.visibility = View.GONE
        val item = ProductVerticalItem(requireContext(), mItems) { product ->
            presenter.onClickItem(product)
        }
        recycler_view.layoutManager = manager
        recycler_view.adapter = item
    }

    override fun showMoreData(items: List<ProductModel>) {
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

    override fun navigateToProductDetailScreen(productModel: ProductModel) {
        NavigatorProduct.showProductDetailScreen(arguments?.apply {
            putSerializable(Constants.PRODUCT_MODEL, productModel)
        })
    }

    override fun updateData(productModel: ProductModel) {
        val index = mItems.indexOfFirst { it.id == productModel.id }
        mItems[index] = productModel
        recycler_view.adapter?.notifyItemChanged(index)
    }

    override fun onClick(v: View) {
        if (v.id == R.id.imv_back) {
            requireActivity().onBackPressed()
        }
    }

    override fun getLayout(): Int {
        return R.layout.fragment_product
    }

    override fun onBackPress() {
        requireActivity().onBackPressed()
    }

    override fun shouldLoadMore() = true
}