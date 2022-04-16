package com.example.client.screens.map.activity

interface IMapsView {
    fun showLoading()
    fun hideLoading()
    fun showErrorMessage(errMessage: Int)
    fun showSuccess()
}