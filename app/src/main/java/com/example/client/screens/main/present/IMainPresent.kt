package com.example.client.screens.main.present

import com.example.client.base.IBasePresenter

interface IMainPresent: IBasePresenter {
    fun getUserActive()
    fun bindData()
    fun navigateToOrderDetail()
}