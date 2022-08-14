package com.example.client.screens.notify

import com.example.client.base.IBaseCollectionView
import com.example.client.models.notify.NotifyModel

interface INotifyView : IBaseCollectionView {
    fun showData(items: List<NotifyModel>)
    fun showMoreData(items: List<NotifyModel>)
    fun showEmptyData()
    fun showErrorMessage(errMessage: Int)
}