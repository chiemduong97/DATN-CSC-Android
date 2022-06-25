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
    private val profileUseCase by lazy { ProfileUseCase.newInstance() }
    private val preferences by lazy { Preferences.newInstance() }

    override fun updateLocation(lat: Double, lng: Double, address: String) {
        mView?.showLoading()
        subscribe(profileUseCase.updateLocation(preferences.profile.email, lat, lng, address), {
            mView?.run {
                hideLoading()
                if (it.is_error) {
                    showErrorMessage(getErrorMessage(it.code))
                    return@subscribe
                }
                preferences.profile = preferences.profile.apply {
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
                showUpdateLocationSuccess()
            }
        }, {
            it.printStackTrace()
            mView?.run {
                hideLoading()
                showErrorMessage(getErrorMessage(1001))
            }
        })
    }

    override fun getCurrentLocation(): LatLng {
        val profile = preferences.profile
        return LatLng(profile.lat, profile.lng)
    }

}