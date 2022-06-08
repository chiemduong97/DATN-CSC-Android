package com.example.client.models.branch

import com.example.client.base.BaseModel

data class BranchModel(
        var id: Int,
        var name: String,
        var lng: Double,
        var lat: Double,
        var address: String,
) : BaseModel()

data class BranchResponse(
        var id: Int?,
        var name: String?,
        var lng: Double?,
        var lat: Double?,
        var address: String?,
) : BaseModel() {
    fun toBranchModel() = BranchModel(
            id = id ?: -1,
            name = name.orEmpty(),
            lng = lng ?: 0.0,
            lat = lat ?: 0.0,
            address = address.orEmpty()
    )
}

fun List<BranchResponse>.toBranches(): List<BranchModel> {
    val branches = arrayListOf<BranchModel>()
    forEach {
        branches.add(it.toBranchModel())
    }
    return branches
}