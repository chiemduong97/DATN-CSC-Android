package com.example.client.screens.branch

import com.example.client.models.branch.BranchModel

interface IBranchView {
    fun showData(items: List<BranchModel>, selected: Int)
    fun showEmptyData()
    fun showLoading()
    fun hideLoading()
    fun showErrorMessage(errMessage: Int)
}