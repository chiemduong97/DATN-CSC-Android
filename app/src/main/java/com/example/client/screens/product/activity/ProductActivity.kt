package com.example.client.screens.product.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.client.R
import com.example.client.app.Constants
import com.example.client.dialog.PrimaryDialog
import com.example.client.models.cart.CartModel
import com.example.client.models.category.CategoryModel
import com.example.client.models.event.Event
import com.example.client.models.product.ProductModel
import com.example.client.screens.product.detail.ProductDetailActivity
import com.example.client.screens.product.item.ProductVerticalItem
import com.example.client.screens.product.present.ProductPresent
import kotlinx.android.synthetic.main.activity_product.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class ProductActivity : AppCompatActivity(), IProductView, View.OnClickListener {
    private var dialog: PrimaryDialog? = null
    private var categoryModel: CategoryModel? = null
    private var present: ProductPresent? = null
    private var method: String? = null
    companion object {
        fun newInstance(context: Context?, categoryModel: CategoryModel, method: String): Intent {
            return Intent(context, ProductActivity::class.java).apply {
                val bundle = Bundle().apply {
                    putString(Constants.PRODUCT_BY, method)
                    putSerializable(Constants.CATEGORY_MODEL, categoryModel)
                }
                putExtras(bundle)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        dialog = PrimaryDialog()
        dialog?.getInstance(this)
        present = ProductPresent(this)
        categoryModel = intent.getSerializableExtra(Constants.CATEGORY_MODEL) as CategoryModel?
        categoryModel?.let {
            present?.loadDataByCategory(it.id)
            tv_title.text = it.name
            swipe_refresh.setOnRefreshListener {
                present?.loadDataByCategory(it.id)
                swipe_refresh.isRefreshing = false
            }
        }
        imv_back.setOnClickListener(this)

    }

    override fun showLoading() {
        rll_loading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        rll_loading.visibility = View.GONE
    }

    override fun showData(items: List<ProductModel>, cart: CartModel) {
        recyclerView.visibility = View.VISIBLE
        imv_empty.visibility = View.GONE
        val manager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = manager
        val item = ProductVerticalItem(this, items, cart.listProduct) {
            startActivity(ProductDetailActivity.newInstance(this, it))
        }
        recyclerView.adapter = item
    }

    override fun showEmptyData() {
        recyclerView.visibility = View.GONE
        imv_empty.visibility = View.VISIBLE
    }

    override fun showErrorMessage(errMessage: Int) {
        recyclerView.visibility = View.GONE
        imv_empty.visibility = View.VISIBLE
        dialog?.run {
            setDescription(getString(errMessage))
            setOKListener {}
            hideBtnCancel()
            show()
        }
    }

    override fun onClick(v: View?) {
        v?.let {
            when (it.id) {
                R.id.imv_back -> {
                    onBackPressed()
                    finish()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: Event) {
        when (event.key) {
            Constants.EventKey.UPDATE_CART -> {
                categoryModel?.let {
                    present?.loadDataByCategory(it.id)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}