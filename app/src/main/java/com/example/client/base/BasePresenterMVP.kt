package com.example.client.base

import android.annotation.SuppressLint
import android.location.Geocoder
import android.util.Log
import com.example.client.R
import com.example.client.app.Constants
import com.example.client.app.Preferences
import com.example.client.app.Res
import com.example.client.app.RxBus
import com.example.client.models.event.Event
import com.example.client.usecase.ProfileUseCase
import com.google.android.gms.location.LocationServices
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.util.*

open class BasePresenterMVP<V : IBaseView>(view: V) : IBasePresenter {
    var mView: V? = null
    private var mCompositeDisposable: CompositeDisposable? = null
    private val profileUseCase by lazy { ProfileUseCase.newInstance() }
    private val preferences by lazy { Preferences.newInstance() }

    init {
        mCompositeDisposable = CompositeDisposable()
        mView = view
    }

    override fun onStart() {}

    override fun onStop() {}

    override fun onDestroy() {
        mView = null
        mCompositeDisposable?.clear()
    }

    protected fun add(disposable: Disposable) {
        mCompositeDisposable?.add(disposable)
    }

    protected fun <T> subscribe(observable: Observable<T>, response: Consumer<T>, throwable: Consumer<Throwable>) {
        val disposable = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response, throwable)
        mCompositeDisposable?.add(disposable)
    }

    override fun onReadyUI() {}

    override fun onViewCreated() {
        onCompositedEventAdded()
    }

    @SuppressLint("MissingPermission")
    override fun updateCurrentLocation() {
        mView?.showLoading()
        Res.context?.let {
            LocationServices.getFusedLocationProviderClient(it).lastLocation.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    mView?.hideLoading()
                    if (task.result == null) {
                        RxBus.newInstance().onNext(Event(Constants.EventKey.UPDATE_LOCATION_WHEN_RUN_APP))
                        return@addOnCompleteListener
                    }

                    val lastLocation = task.result
                    val geoCoder = Geocoder(it, Locale.getDefault())
                    val addresses = geoCoder.getFromLocation(lastLocation.latitude, lastLocation.longitude, 1);
                    val lastAddress = addresses[0].getAddressLine(0)
                    preferences.orderLocation = preferences.orderLocation.apply {
                        lat = lastLocation.latitude
                        lng = lastLocation.longitude
                        address = lastAddress
                    }
                    RxBus.newInstance().onNext(Event(Constants.EventKey.UPDATE_LOCATION_WHEN_RUN_APP))

                } else {
                    Log.d("Duong", "Current location is null. Using defaults.")
                    Log.e("Duong", "Exception: %s", task.exception)
                }
            }
        }

    }

    protected open fun onCompositedEventAdded() {}

    protected fun getErrorMessage(code: Int): Int {
        var errMessage = -1
        when (code) {
            Constants.ErrorCode.ERROR_1001 -> errMessage = R.string.err_code_1001
            Constants.ErrorCode.ERROR_1002 -> errMessage = R.string.err_code_1002
            Constants.ErrorCode.ERROR_1003 -> errMessage = R.string.err_code_1003
            Constants.ErrorCode.ERROR_1004 -> errMessage = R.string.err_code_1004
            Constants.ErrorCode.ERROR_1005 -> errMessage = R.string.err_code_1005
            Constants.ErrorCode.ERROR_1006 -> errMessage = R.string.err_code_1006
            Constants.ErrorCode.ERROR_1007 -> errMessage = R.string.err_code_1007
            Constants.ErrorCode.ERROR_1008 -> errMessage = R.string.err_code_1008
            Constants.ErrorCode.ERROR_1009 -> errMessage = R.string.err_code_1009
            Constants.ErrorCode.ERROR_1010 -> errMessage = R.string.err_code_1010
            Constants.ErrorCode.ERROR_1011 -> errMessage = R.string.err_code_1011
            Constants.ErrorCode.ERROR_1012 -> errMessage = R.string.err_code_1012
            Constants.ErrorCode.ERROR_1013 -> errMessage = R.string.err_code_1013
            Constants.ErrorCode.ERROR_1014 -> errMessage = R.string.err_code_1014
            Constants.ErrorCode.ERROR_1015 -> errMessage = R.string.err_code_1015
            Constants.ErrorCode.ERROR_1016 -> errMessage = R.string.err_code_1016
            Constants.ErrorCode.ERROR_1017 -> errMessage = R.string.err_code_1017
            Constants.ErrorCode.ERROR_1018 -> errMessage = R.string.err_code_1018
            Constants.ErrorCode.ERROR_1019 -> errMessage = R.string.err_code_1019

        }
        return errMessage
    }

}