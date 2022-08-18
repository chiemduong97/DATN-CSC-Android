package com.example.client.screens.product.detail

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.client.R
import com.example.client.app.Constants
import com.example.client.base.BaseFragmentMVP
import com.example.client.dialog.AddToCartDialog
import com.example.client.dialog.OptionAddToCartListener
import com.example.client.models.cart.CartProductModel
import com.example.client.models.product.ProductModel
import com.example.client.screens.cart.activity.CartActivity
import com.example.client.screens.product.detail.present.IProductDetailPresent
import com.example.client.screens.product.detail.present.ProductDetailPresent
import com.example.client.screens.product.item.ProductHorizontalItem
import com.example.client.screens.product.navigate.NavigatorProduct
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import kotlinx.android.synthetic.main.fragment_product_detail.*
import java.text.NumberFormat
import java.util.*

class ProductDetailFragment : BaseFragmentMVP<IProductDetailPresent>(), IProductDetailView, View.OnClickListener, OptionAddToCartListener {

    private var scrollTop = true

    companion object {
        fun newInstance(args: Bundle?) = ProductDetailFragment().apply {
            arguments = args
        }
    }

    private val product by lazy { arguments?.getSerializable(Constants.BundleKey.PRODUCT_MODEL) as ProductModel }

    override val presenter: IProductDetailPresent
        get() = ProductDetailPresent(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_product_detail, null)
    }

    override fun bindData() {
        mPresenter?.run {
            loadDataByCategory(product.category)
            getCartFromRes()
        }
    }

    override fun bindComponent() {
        Glide.with(this).asBitmap().placeholder(R.drawable.ic_category_default)
                .load(product.avatar)
                .into(view_icon)
        tv_title.text = product.name
        tv_name.text = product.name
        tv_price.text = NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(product.price)
        tv_description.text = product.description
        if (product.quantity > 1) {
            tv_add_to_cart.isEnabled = true
            tv_add_to_cart.setBackgroundResource(R.drawable.bg_btn)
            tv_quantity.text = getString(R.string.text_product_quantity, product.quantity)
        } else {
            tv_add_to_cart.isEnabled = false
            tv_add_to_cart.setBackgroundResource(R.drawable.bg_btn_disable)
            tv_quantity.text = getString(R.string.text_product_quantity_0)
        }
    }

    override fun bindEvent() {
        imv_back.setOnClickListener(this)
        cv_cart_place.setOnClickListener(this)
        tv_add_to_cart.setOnClickListener(this)
        tv_more.setOnClickListener(this)
        appbar_layout.addOnOffsetChangedListener(OnOffsetChangedListener { _, verticalOffset ->
            when (verticalOffset) {
                -collapsing_toolbar.height + toolbar.height -> {
                    tv_title.visibility = View.VISIBLE
                    imv_back.setColorFilter(Color.BLACK)
                    imv_share.setColorFilter(Color.BLACK)
                    scrollTop = false
                }
                else -> {
                    if (!scrollTop) {
                        tv_title.visibility = View.INVISIBLE
                        imv_back.setColorFilter(Color.WHITE)
                        imv_share.setColorFilter(Color.WHITE)
                        scrollTop = true
                    }
                }
            }
        })
    }

    override fun showListProduct(items: List<ProductModel>) {
        recycler_view.visibility = View.VISIBLE
        imv_empty.visibility = View.GONE
        val manager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recycler_view.layoutManager = manager
        val item = ProductHorizontalItem(
                requireContext(), items.filter { item -> item.id != product.id } + ProductModel(),
                product.category,
                {
                    NavigatorProduct.popFragment()
                    NavigatorProduct.showProductDetailScreen(arguments?.apply {
                        putSerializable(Constants.BundleKey.PRODUCT_MODEL, it)
                    })
                },
                {
                    showAddToCartDialog(it)
                },
                {
                    NavigatorProduct.showProductScreen(arguments?.apply {
                        putSerializable(Constants.BundleKey.CATEGORY_MODEL, product.category)
                    })
                }
        )
        recycler_view.adapter = item
    }

    override fun showEmptyData() {
        recycler_view.visibility = View.GONE
        imv_empty.visibility = View.VISIBLE
    }

    override fun showLoading() {
        rll_loading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        rll_loading.visibility = View.GONE
    }

    override fun onBackPress() {
        requireActivity().onBackPressed()
    }

    override fun showCart(quantity: Int) {
        rll_cart.visibility = View.VISIBLE
        tv_cart_quantity.text = quantity.toString()
    }

    override fun hideCart() {
        rll_cart.visibility = View.GONE
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.imv_back -> requireActivity().onBackPressed()
            R.id.tv_add_to_cart -> {
                showAddToCartDialog(product)
            }
            R.id.cv_cart_place -> {
                startActivity(CartActivity.newInstance(requireActivity()))
            }
            R.id.tv_more -> {
                NavigatorProduct.showProductScreen(arguments?.apply {
                    putSerializable(Constants.BundleKey.CATEGORY_MODEL, product.category)
                })
            }
        }
    }


    override fun addToCart(cartProduct: CartProductModel) {
        mPresenter?.run {
            addToCart(cartProduct)
            getCartFromRes()
        }
    }

    override fun showProductDetail(product: ProductModel) {
        NavigatorProduct.popFragment()
        NavigatorProduct.showProductDetailScreen(arguments?.apply {
            putSerializable(Constants.BundleKey.PRODUCT_MODEL, product)
        })
    }

    private fun showAddToCartDialog(product: ProductModel) {
        val dialog = AddToCartDialog.newInstance(product)
        dialog.setListener(this)
        dialog.show(childFragmentManager)
    }

}