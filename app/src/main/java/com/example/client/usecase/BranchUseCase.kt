package com.example.client.usecase

import com.example.client.api.ApiClient
import com.example.client.api.service.BranchService
import com.example.client.models.branch.BranchResponse
import com.example.client.models.response.BaseResponse
import io.reactivex.Observable

class BranchUseCase {
    private val branchService by lazy { ApiClient.newInstance().create(BranchService::class.java) }
    companion object {
        fun newInstance() = BranchUseCase()
    }
    fun getBranches(): Observable<BaseResponse<List<BranchResponse>>> {
        return branchService.getBranches()
    }
}