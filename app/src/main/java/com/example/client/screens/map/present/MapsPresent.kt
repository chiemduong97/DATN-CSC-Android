package com.example.client.screens.map.present

import com.example.client.app.Constants
import com.example.client.app.Preferences
import com.example.client.app.RxBus
import com.example.client.base.BasePresenterMVP
import com.example.client.models.event.Event
import com.example.client.screens.map.activity.IMapsView
import com.example.client.usecase.ProfileUseCase
import com.google.android.gms.maps.model.LatLng

class MapsPresent(mView: IMapsView): BasePresenterMVP<IMapsView>(mView), IMapsPresent {
    private val preferences by lazy { Preferences.newInstance() }

    override fun updateLocation(lat: Double, lng: Double, address: String) {
        preferences.orderLocation = preferences.orderLocation.apply {
            this.lat = lat
            this.lng = lng
            this.address = address
        }
        preferences.cart = preferences.cart.apply {
            this.order_lat = lat
            this.order_lng = lng
            this.order_address = address
        }
        RxBus.newInstance().onNext(Event(Constants.EventKey.UPDATE_LOCATION))
        mView?.showUpdateLocationSuccess()
    }

    override fun getCurrentLocation(): LatLng {
        val orderLocation = preferences.orderLocation
        return LatLng(orderLocation.lat, orderLocation.lng)
    }

}