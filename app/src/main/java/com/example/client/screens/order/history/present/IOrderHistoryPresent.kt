package com.example.client.screens.order.history.present

import com.example.client.base.IBaseCollectionPresenter
import com.example.client.models.loading.LoadingMode

interface IOrderHistoryPresent: IBaseCollectionPresenter {
    fun bindData()
    fun getOrders(page: Int, loadingMode: LoadingMode)
    fun onClickItem(orderCode: String)
}