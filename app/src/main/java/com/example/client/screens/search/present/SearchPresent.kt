package com.example.client.screens.search.present

import com.example.client.app.Constants
import com.example.client.app.Preferences
import com.example.client.app.RxBus
import com.example.client.base.BaseCollectionPresenter
import com.example.client.models.event.ValueEvent
import com.example.client.models.loading.LoadingMode
import com.example.client.models.product.ProductModel
import com.example.client.models.product.checkCart
import com.example.client.models.product.toProducts
import com.example.client.screens.search.activity.ISearchView
import com.example.client.usecase.CategoryUseCase
import com.example.client.usecase.ProductUseCase

class SearchPresent(mView: ISearchView) : BaseCollectionPresenter<ISearchView>(mView), ISearchPresent {
    private val productUseCase by lazy { ProductUseCase.newInstance() }
    private val categoryUseCase by lazy { CategoryUseCase.newInstance() }
    private val preferences by lazy { Preferences.newInstance() }
    private var mQuery: String? = null

    override fun searchProducts(query: String) {
        mQuery = query
        getProducts(query, page, LoadingMode.LOAD)
    }



    override fun onClickItem(product: ProductModel) {
        mView?.showLoading()
        subscribe(categoryUseCase.getCategory(product.category.id), {
            mView?.run {
                hideLoading()
                if (it.is_error) {
                    showErrorMessage(getErrorMessage(it.code))
                    return@subscribe
                }
                toProductDetailScreen(it.data.toCategoryModel(), product)
            }
        }, {
            it.printStackTrace()
            mView?.run {
                hideLoading()
                showErrorMessage(getErrorMessage(1001))
            }
        })
    }

    private fun getProducts(query: String, page: Int, loadingMode: LoadingMode) {
        mView?.showLoading()
        subscribe(productUseCase.filterProducts(query, page, limit), {
            mView?.run {
                hideLoading()
                if (it.is_error) {
                    showEmptyData()
                    onLoadMoreComplete()
                    return@subscribe
                }
                when (loadingMode) {
                    LoadingMode.LOAD -> {
                        if (it.data.isEmpty()) showEmptyData()
                        else {
                            showData(it.data.toProducts().checkCart(preferences.cart))
                            loadMore = it.load_more
                        }
                    }
                    LoadingMode.LOAD_MORE -> {
                        showMoreData(it.data.toProducts().checkCart(preferences.cart))
                        loadMore = it.load_more
                        onLoadMoreComplete()
                    }
                }
            }
        }, {
            it.printStackTrace()
            mView?.run {
                hideLoading()
                if (loadingMode == LoadingMode.LOAD) {
                    showEmptyData()
                }
            }
            onLoadMoreComplete()
        })
    }

    override fun onRefresh() {
        super.onRefresh()
        mQuery?.let { getProducts(it, page, LoadingMode.LOAD) }
    }

    override fun invokeLoadMore(page: Int) {
        super.invokeLoadMore(page)
        mQuery?.let { getProducts(it, page, LoadingMode.LOAD_MORE) }
    }

    override fun onCompositedEventAdded() {
        super.onCompositedEventAdded()
        add(RxBus.newInstance().subscribe {
            when (it.key) {
                Constants.EventKey.UPDATE_ADD_TO_CART_PRODUCT -> {
                    val productModel = (it as ValueEvent<*>).value
                    mView?.updateData(productModel as ProductModel)
                }
            }
        })
    }
}