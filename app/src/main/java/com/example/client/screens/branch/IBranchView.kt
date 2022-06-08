package com.example.client.screens.branch

import com.example.client.base.IBaseCollectionView
import com.example.client.models.branch.BranchModel

interface IBranchView: IBaseCollectionView {
    fun showData(items: List<BranchModel>, selected: Int)
    fun showEmptyData()
    fun showErrorMessage(errMessage: Int)
}