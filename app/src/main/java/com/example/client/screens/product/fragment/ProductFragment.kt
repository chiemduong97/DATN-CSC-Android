package com.example.client.screens.product.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.client.R
import com.example.client.app.Constants
import com.example.client.base.BaseCollectionFragment
import com.example.client.models.category.CategoryModel
import com.example.client.models.product.ProductModel
import com.example.client.screens.product.activity.IProductView
import com.example.client.screens.product.item.ProductVerticalItem
import com.example.client.screens.product.navigate.NavigatorProduct
import com.example.client.screens.product.present.IProductPresent
import com.example.client.screens.product.present.ProductPresent
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

    private val category by lazy { arguments?.getSerializable(Constants.BundleKey.CATEGORY_MODEL) as? CategoryModel }
    override val presenter: ProductPresent
        get() = ProductPresent(this)

    override fun bindData() {
        category?.let { mPresenter?.binData(it) }
    }

    override fun bindComponent() {
        tv_title.text = category?.name
    }

    override fun bindEvent() {
        imv_back.setOnClickListener(this)
    }

    override fun showData(items: List<ProductModel>) {
        mItems = items as ArrayList<ProductModel>
        recycler_view.visibility = View.VISIBLE
        imv_empty.visibility = View.GONE
        val item = ProductVerticalItem(requireContext(), mItems) { product ->
            mPresenter?.onClickItem(product)
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
            putSerializable(Constants.BundleKey.PRODUCT_MODEL, productModel)
        })
    }

    override fun updateData(productModel: ProductModel) {
        val index = mItems.indexOfFirst { it.id == productModel.id }
        if (index == -1) return
        mItems[index] = mItems[index].apply { addToCart = productModel.addToCart }
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