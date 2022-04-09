package com.example.client.screens.branch.present

import com.example.client.models.branch.BranchModel

interface IBranchPresent {
    fun loadDataFromRes()
    fun saveBranch(branch: BranchModel)
}