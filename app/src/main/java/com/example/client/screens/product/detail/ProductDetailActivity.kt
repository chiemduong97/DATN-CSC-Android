package com.example.client.screens.product.detail

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.client.R
import com.example.client.app.Constants
import com.example.client.dialog.AddToCartDialog
import com.example.client.dialog.OptionAddToCartListener
import com.example.client.dialog.PrimaryDialog
import com.example.client.models.cart.CartProductModel
import com.example.client.models.event.Event
import com.example.client.models.product.ProductModel
import com.example.client.screens.cart.activity.CartActivity
import com.example.client.screens.product.detail.present.ProductDetailPresent
import com.example.client.screens.product.item.ProductHorizontalItem
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import kotlinx.android.synthetic.main.activity_product_detail.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.NumberFormat
import java.util.*

class ProductDetailActivity : AppCompatActivity(), IProductDetailView, View.OnClickListener, OptionAddToCartListener {

    private var productModel: ProductModel? = null
    private var scrollTop = true
    private var dialog: PrimaryDialog? = null
    private var present: ProductDetailPresent? = null

    companion object {
        fun newInstance(context: Context?, productModel: ProductModel): Intent {
            return Intent(context, ProductDetailActivity::class.java).apply {
                val bundle = Bundle().apply {
                    putSerializable(Constants.PRODUCT_MODEL, productModel)
                }
                putExtras(bundle)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        dialog = PrimaryDialog()
        dialog?.getInstance(this)
        present = ProductDetailPresent(this)

        productModel = intent.getSerializableExtra(Constants.PRODUCT_MODEL) as ProductModel?
        productModel?.let {
            loadData(it)
            present?.loadDataByCategory(it.category_id)
        }
        present?.getCartFromRes()

        imv_back.setOnClickListener(this)
        cv_cart_place.setOnClickListener(this)
        tv_add_to_cart.setOnClickListener(this)

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

    override fun loadData(productModel: ProductModel) {
        Glide.with(this).asBitmap().placeholder(R.drawable.subject_default)
                .load(productModel.avatar)
                .into(imv_avatar)
        tv_title.text = productModel.name
        tv_name.text = productModel.name
        tv_price.text = NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(productModel.price)
        tv_product_quantity.text = getString(R.string.text_product_quantity).replace("%s", productModel.quantity.toString())
        tv_description.text = productModel.description
        if (productModel.quantity > 1) {
            tv_add_to_cart.isEnabled = true
            tv_add_to_cart.setBackgroundResource(R.drawable.bg_btn)
            tv_product_quantity.text = getString(R.string.text_product_quantity).replace("%s", productModel.quantity.toString())
        } else {
            tv_add_to_cart.isEnabled = false
            tv_add_to_cart.setBackgroundResource(R.drawable.bg_btn_disable)
            tv_product_quantity.text = getString(R.string.text_product_quantity_0)
        }
    }

    override fun showListProduct(items: List<ProductModel>) {
        recyclerView.visibility = View.VISIBLE
        imv_empty.visibility = View.GONE
        val manager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = manager
        val item = ProductHorizontalItem(this, items) {
            startActivity(newInstance(this, it))
        }
        recyclerView.adapter = item
    }

    override fun showListEmpty() {
        recyclerView.visibility = View.GONE
        imv_empty.visibility = View.VISIBLE
    }

    override fun showLoading() {
        rll_loading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        rll_loading.visibility = View.GONE
    }

    override fun showCart(quantity: Int) {
        rll_cart.visibility = View.VISIBLE
        tv_quantity.text = quantity.toString()
    }

    override fun hideCart() {
        rll_cart.visibility = View.GONE
    }

    override fun onClick(v: View?) {
        v?.let {
            when (it.id) {
                R.id.imv_back -> {
                    onBackPressed()
                    finish()
                }
                R.id.tv_add_to_cart -> {
                    productModel?.let { product ->
                        val dialog = AddToCartDialog.newInstance(product)
                        dialog.setListener(this)
                        dialog.show(supportFragmentManager, dialog.tag)
                    }
                }
                R.id.cv_cart_place -> {
                    startActivity(Intent(CartActivity.newInstance(this)))
                }
                else -> {

                }
            }
        }
    }


    override fun addToCart(cartProduct: CartProductModel) {
        present?.run {
            addToCart(cartProduct)
            getCartFromRes()
        }
    }

    override fun onStart() {
        super.onStart()
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: Event) {
        when (event.key) {
            Constants.EventKey.UPDATE_CART -> present?.getCartFromRes()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}