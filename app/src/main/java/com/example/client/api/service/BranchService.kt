package com.example.client.api.service

import com.example.client.models.branch.BranchModel
import com.example.client.models.branch.BranchResponse
import com.example.client.models.response.BaseResponse
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BranchService {
    @GET("api/branch/branch_getAll.php")
    fun getBranches(): Observable<BaseResponse<List<BranchResponse>>>

    @GET("api/branch/branch_getByID.php")
    fun getById(@Query("id") id: Int): Call<BranchModel>
}