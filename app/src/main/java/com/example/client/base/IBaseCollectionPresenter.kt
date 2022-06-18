package com.example.client.base

interface IBaseCollectionPresenter: IBasePresenter {
    fun loadMore()
    fun onRefresh()
}