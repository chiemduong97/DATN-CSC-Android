package com.example.client.api.service

import com.example.client.models.branch.BranchModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BranchService {
    @GET("api/branch/branch_getAll.php")
    fun getAll(): Call<List<BranchModel>>

    @GET("api/branch/branch_getByID.php")
    fun getById(@Query("id") id: Int): Call<BranchModel>
}