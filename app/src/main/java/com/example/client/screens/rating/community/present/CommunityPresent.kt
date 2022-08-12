package com.example.client.screens.rating.community.present

import com.example.client.base.BaseCollectionPresenter
import com.example.client.models.loading.LoadingMode
import com.example.client.models.rating.RatingModel
import com.example.client.models.rating.toRatings
import com.example.client.screens.product.present.ProductPresent
import com.example.client.screens.rating.community.fragment.ICommunityView
import com.example.client.usecase.RatingUseCase

class CommunityPresent(mView: ICommunityView) : BaseCollectionPresenter<ICommunityView>(mView), ICommunityPresent {
    private val ratingUseCase by lazy { RatingUseCase.newInstance() }

    override fun bindData() {
        getRatings(page, LoadingMode.LOAD)
    }


    private fun getRatings(page: Int, loadingMode: LoadingMode) {
        if (loadingMode == LoadingMode.LOAD) mView?.showLoading()
        subscribe(ratingUseCase.getRatings(page, limit), {
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
                            showData(it.data.toRatings())
                            loadMore = it.load_more
                        }
                    }
                    LoadingMode.LOAD_MORE -> {
                        showMoreData(it.data.toRatings())
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

    override fun onClickItem(rating: RatingModel) {
        mView?.navigateToRatingDetailScreen(rating)
    }


    override fun invokeLoadMore(page: Int) {
        super.invokeLoadMore(page)
        getRatings(page, LoadingMode.LOAD_MORE)
    }

    override fun onRefresh() {
        super.onRefresh()
        bindData()
    }
}