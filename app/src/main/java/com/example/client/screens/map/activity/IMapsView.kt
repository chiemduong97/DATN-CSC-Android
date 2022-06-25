package com.example.client.screens.map.activity

import com.example.client.base.IBaseView

interface IMapsView: IBaseView {
    fun showErrorMessage(errMessage: Int)
    fun showUpdateLocationSuccess()
}