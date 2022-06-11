package com.example.client.screens.product.present

import com.example.client.app.Preferences
import com.example.client.base.BaseCollectionPresenter
import com.example.client.models.cart.CartModel
import com.example.client.models.category.CategoryModel
import com.example.client.models.loading.LoadingMode
import com.example.client.models.product.ProductModel
import com.example.client.models.product.toProducts
import com.example.client.screens.product.activity.IProductView
import com.example.client.usecase.ProductUseCase


class ProductPresent(mView: IProductView) : BaseCollectionPresenter<IProductView>(mView), IProductPresent {
    private val productUseCase by lazy { ProductUseCase.newInstance() }

    companion object {
        var mCategoryModel: CategoryModel? = null
    }

    override fun binData(categoryModel: CategoryModel) {
        mCategoryModel = categoryModel
        loadDataByCategory(categoryModel.id, Preferences.getInstance().branch.id, page, LoadingMode.LOAD)
    }

    override fun loadDataByCategory(category_id: Int, branch_id: Int, page: Int, loadingMode: LoadingMode) {
        if (loadingMode == LoadingMode.LOAD) mView?.showLoading()
        subscribe(productUseCase.getByCategory(category_id, branch_id, page, limit), {
            mView?.run {
                hideLoading()
                if (it.is_error || it.data.isEmpty()) {
                    showEmptyData()
                    onLoadMoreComplete()
                    return@subscribe
                }
                when (loadingMode) {
                    LoadingMode.LOAD -> {
                        showData(it.data.toProducts(), Preferences.getInstance().cart
                                ?: CartModel(arrayListOf()))
                        loadMore = it.load_more
                    }
                    LoadingMode.LOAD_MORE -> {
                        showMoreData(it.data.toProducts(), Preferences.getInstance().cart
                                ?: CartModel(arrayListOf()))
                        loadMore = it.load_more
                        onLoadMoreComplete()
                    }
                }
            }
        }, {
            it.printStackTrace()
            mView?.run {
                hideLoading()
                showEmptyData()
            }
            onLoadMoreComplete()
        })
    }

    override fun onClickItem(productModel: ProductModel) {

    }

    override fun invokeLoadMore(page: Int) {
        super.invokeLoadMore(page)
        mCategoryModel?.let { loadDataByCategory(it.id, Preferences.getInstance().branch.id, page, LoadingMode.LOAD_MORE) }
    }
}