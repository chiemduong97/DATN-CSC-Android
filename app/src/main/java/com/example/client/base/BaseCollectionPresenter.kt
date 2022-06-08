package com.example.client.base

abstract class BaseCollectionPresenter<V : IBaseCollectionView>(view: V) : BasePresenter<V>(view), IBaseCollectionPresenter {
    private var isLoadingMore = false
    private var page = 1
    private var limit = 10
    override fun loadMore() {
        if (isLoadingMore) return
        isLoadingMore = true
        mView?.addLoadMore()
        page ++
        limit += 10
        invokeLoadMore(page, limit)
    }
    protected open fun onLoadMoreComplete() {
        if (isLoadingMore) mView?.removeLoadMore()
        isLoadingMore = false
    }

    protected open fun invokeLoadMore(page: Int, limit: Int) {}

}