package com.example.client.base

abstract class BaseCollectionPresenter<V : IBaseCollectionView>(view: V) : BasePresenterMVP<V>(view), IBaseCollectionPresenter {
    private var isLoadingMore = false
    open var page = 1
    protected val limit = 10
    protected var loadMore = true
    override fun loadMore() {
        if (isLoadingMore || !loadMore) return
        isLoadingMore = true
        mView?.addLoadMore()
        page ++
        invokeLoadMore(page)
    }
    protected open fun onLoadMoreComplete() {
        if (isLoadingMore) mView?.removeLoadMore()
        isLoadingMore = false
    }

    protected open fun invokeLoadMore(page: Int) {}

}