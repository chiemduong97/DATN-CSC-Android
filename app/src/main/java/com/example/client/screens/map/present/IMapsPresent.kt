package com.example.client.screens.map.present

import com.google.android.gms.maps.model.LatLng

interface IMapsPresent {
    fun updateLocation(latitude: Double, longitude: Double, address: String)
    fun getCurrentLocation() : LatLng
}