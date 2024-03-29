package com.example.client.screens.product.present

import com.example.client.app.Constants
import com.example.client.app.Preferences
import com.example.client.app.RxBus
import com.example.client.base.BaseCollectionPresenter
import com.example.client.models.cart.CartModel
import com.example.client.models.category.CategoryModel
import com.example.client.models.event.ValueEvent
import com.example.client.models.loading.LoadingMode
import com.example.client.models.category.HomeSectionModel
import com.example.client.models.product.ProductModel
import com.example.client.models.product.checkCart
import com.example.client.models.product.toProducts
import com.example.client.screens.product.activity.IProductView
import com.example.client.usecase.ProductUseCase
import kotlinx.android.synthetic.main.fragment_product.*


class ProductPresent(mView: IProductView) : BaseCollectionPresenter<IProductView>(mView), IProductPresent {
    private val productUseCase by lazy { ProductUseCase.newInstance() }
    private val preferences by lazy { Preferences.newInstance() }
    private var mCategoryModel: CategoryModel? = null

    override fun binData(categoryModel: CategoryModel) {
        mCategoryModel = categoryModel
        if (categoryModel is HomeSectionModel) getProductsByUrl(categoryModel.url, page, LoadingMode.LOAD)
        else getProducts(categoryModel.id, page, LoadingMode.LOAD)
    }

    private fun getProductsByUrl(url: String, page: Int, loadingMode: LoadingMode) {
        if (loadingMode == LoadingMode.LOAD) mView?.showLoading()
        subscribe(productUseCase.getProductsByUrl(url, page, limit), {
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

    private fun getProducts(category_id: Int, page: Int, loadingMode: LoadingMode) {
        if (loadingMode == LoadingMode.LOAD) mView?.showLoading()
        subscribe(productUseCase.getProducts(category_id, page, limit), {
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

    override fun onClickItem(productModel: ProductModel) {
        mView?.navigateToProductDetailScreen(productModel)
    }

    override fun getCartFromRes(): CartModel {
        return preferences.cart
    }

    override fun invokeLoadMore(page: Int) {
        super.invokeLoadMore(page)
        mCategoryModel?.let {
            if (it is HomeSectionModel) getProductsByUrl(it.url, page, LoadingMode.LOAD_MORE)
            else getProducts(it.id, page, LoadingMode.LOAD_MORE)
        }
    }

    override fun onCompositedEventAdded() {
        super.onCompositedEventAdded()
        add(RxBus.newInstance().subscribe {
            when (it.key) {
                Constants.EventKey.UPDATE_ADD_TO_CART_PRODUCT -> {
                    val productModel = (it as ValueEvent<*>).value
                    mView?.updateData(productModel as ProductModel)
                }
                Constants.EventKey.DELETE_CART -> {
                    mCategoryModel?.let { cate -> getProducts(cate.id, page, LoadingMode.LOAD) }
                }
            }
        })
    }

    override fun onRefresh() {
        super.onRefresh()
        mCategoryModel?.let { binData(it) }
    }
}