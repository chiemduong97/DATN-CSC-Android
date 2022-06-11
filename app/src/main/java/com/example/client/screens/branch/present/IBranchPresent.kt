package com.example.client.screens.branch.present

import com.example.client.base.IBaseCollectionPresenter
import com.example.client.base.IBasePresenter
import com.example.client.models.branch.BranchModel

interface IBranchPresent: IBaseCollectionPresenter{
    fun binData()
    fun getBranches()
    fun saveBranch(branch: BranchModel)
}