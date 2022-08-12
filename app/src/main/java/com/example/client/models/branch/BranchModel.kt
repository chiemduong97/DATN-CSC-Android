package com.example.client.models.branch

import com.example.client.base.BaseModel
import com.google.android.gms.maps.model.LatLng
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

data class BranchModel(
        val id: Int = -1,
        val name: String = "",
        val lng: Double = 0.0,
        val lat: Double = 0.0,
        val address: String = "",
        val distance: Double = 0.0,
) : BaseModel()

data class BranchResponse(
        val id: Int?,
        val name: String?,
        val lng: Double?,
        val lat: Double?,
        val address: String?,
        val distance: Double?
) : BaseModel() {
    fun toBranchModel(userLocation: LatLng) = BranchModel(
            id = id ?: -1,
            name = name.orEmpty(),
            lng = lng ?: 0.0,
            lat = lat ?: 0.0,
            address = address.orEmpty(),
            distance = getDistance(userLocation, LatLng(lat ?: 0.0, lng ?: 0.0))
    )
}

private fun getDistance(userLocation: LatLng, branchLocation: LatLng): Double {
    val radius = 6371.0
    val dLat: Double = (branchLocation.latitude - userLocation.latitude) * (Math.PI / 180)
    val dLon: Double = (branchLocation.longitude - userLocation.longitude) * (Math.PI / 180)
    val la1ToRad: Double = userLocation.latitude * (Math.PI / 180)
    val la2ToRad: Double = branchLocation.latitude * (Math.PI / 180)
    val a = sin(dLat / 2) * sin(dLat / 2) + (cos(la1ToRad) * cos(la2ToRad) * sin(dLon / 2) * sin(dLon / 2))
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return radius * c
}

fun List<BranchResponse>.toBranches(userLocation: LatLng) = map { it.toBranchModel(userLocation) }