package com.example.client.screens.search.fragment

import android.text.TextUtils
import android.view.View
import android.widget.SearchView
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.client.R
import com.example.client.base.BaseCollectionFragment
import com.example.client.models.category.CategoryModel
import com.example.client.models.product.ProductModel
import com.example.client.screens.product.activity.ProductActivity
import com.example.client.screens.product.item.ProductVerticalItem
import com.example.client.screens.search.activity.ISearchView
import com.example.client.screens.search.present.ISearchPresent
import com.example.client.screens.search.present.SearchPresent
import com.jakewharton.rxbinding2.widget.RxSearchView
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_search.*
import java.util.concurrent.TimeUnit

class SearchFragment : BaseCollectionFragment<ISearchPresent>(), ISearchView, SearchView.OnQueryTextListener {

    companion object {
        @JvmStatic
        fun newInstance() = SearchFragment()
    }

    private var mItems = arrayListOf<ProductModel>()
    private val manager by lazy { LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false) }

    override fun bindComponent() {
        val searchTextId = search_view.context.resources.getIdentifier("android:id/search_src_text", null, null)
        TextViewCompat.setTextAppearance(search_view.findViewById(searchTextId), R.style.search_text)
    }

    override fun bindEvent() {
        search_view.setOnQueryTextListener(this)
        imv_back.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    override val presenter: ISearchPresent
        get() = SearchPresent(this)

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
        imv_empty.visibility = View.VISIBLE
        recycler_view.visibility = View.GONE
    }

    override fun updateData(productModel: ProductModel) {
        val index = mItems.indexOfFirst { it.id == productModel.id }
        if (index == -1) return
        mItems[index] = mItems[index].apply { addToCart = productModel.addToCart }
        recycler_view.adapter?.notifyItemChanged(index)
    }

    override fun toProductDetailScreen(cate: CategoryModel, prod: ProductModel) {
        startActivity(ProductActivity.newInstance(requireActivity(), prod, true))
    }

    override fun showErrorMessage(errMessage: Int) {
        showToastMessage(getString(errMessage))
    }

    override fun onBackPress() {
        requireActivity().onBackPressed()
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        mPresenter?.searchProducts(query)
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        if (newText.trim { it <= ' ' }.length >= 2) {
            add(RxSearchView.queryTextChanges(search_view)
                    .debounce(500, TimeUnit.MILLISECONDS)
                    .filter { str: CharSequence -> !TextUtils.isEmpty(str.toString().trim { it <= ' ' }) && str.toString().trim { it <= ' ' }.isNotEmpty() }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        mPresenter?.searchProducts(it.toString())
                    }, {
                        it.printStackTrace()
                    })
            )
        }
        return true
    }

    override fun getLayout(): Int {
        return R.layout.fragment_search
    }

    override fun shouldLoadMore() = true

}