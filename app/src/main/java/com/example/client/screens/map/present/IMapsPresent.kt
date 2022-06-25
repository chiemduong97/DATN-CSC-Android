package com.example.client.screens.map.present

import com.example.client.base.IBasePresenter
import com.google.android.gms.maps.model.LatLng

interface IMapsPresent: IBasePresenter {
    fun updateLocation(lat: Double, lng: Double, address: String)
    fun getCurrentLocation() : LatLng
}