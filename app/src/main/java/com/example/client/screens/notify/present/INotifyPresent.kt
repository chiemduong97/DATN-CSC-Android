package com.example.client.screens.notify.present

import com.example.client.base.IBaseCollectionPresenter
import com.example.client.models.loading.LoadingMode

interface INotifyPresent : IBaseCollectionPresenter {
    fun bindData()
    fun getNotifies(page: Int, loadingMode: LoadingMode)
}