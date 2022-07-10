package com.example.client.screens.map.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.client.R
import com.example.client.base.BaseActivityMVP
import com.example.client.dialog.PrimaryDialog
import com.example.client.screens.map.present.IMapsPresent
import com.example.client.screens.map.present.MapsPresent
import com.example.client.utils.LocationUtils
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*
import java.util.*


class MapsActivity : BaseActivityMVP<IMapsPresent>(), OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMyLocationButtonClickListener, IMapsView, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private lateinit var mMap: GoogleMap
    private var fused: FusedLocationProviderClient? = null
    private var lastLocation = Location("")
    private var locationPermissionGranted = false
    private val DEFAULT_ZOOM by lazy { 12 }
    private var defaultLocation = LatLng(10.8529727, 106.6295453)

    companion object {
        fun newInstance(from: Activity): Intent {
            return Intent(from, MapsActivity::class.java)
        }

        val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION by lazy { 999 }
    }

    override val presenter: IMapsPresent
        get() = MapsPresent(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        fused = LocationServices.getFusedLocationProviderClient(this)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMyLocationButtonClickListener(this)
        tv_get_location.setOnClickListener(this)
        imv_back.setOnClickListener(this)
        requestLocationPermission()
        if (locationPermissionGranted) {
            updateLocationUI()
            presenter.getCurrentLocation().let {
                mMap.clear()
                lastLocation.apply {
                    latitude = it.latitude
                    longitude = it.longitude
                }
                val geoCoder = Geocoder(this@MapsActivity, Locale.getDefault())
                val addresses = geoCoder.getFromLocation(it.latitude, it.longitude, 1);
                val address = addresses[0].getAddressLine(0)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it, DEFAULT_ZOOM.toFloat()))
                mMap.addMarker(MarkerOptions().position(it).title(address))
            }
        }
        mMap.setOnMapClickListener {
            try {
                mMap.clear()
                lastLocation.apply {
                    latitude = it.latitude
                    longitude = it.longitude
                }
                val geoCoder = Geocoder(this@MapsActivity, Locale.getDefault())
                val addresses = geoCoder.getFromLocation(it.latitude, it.longitude, 1);
                val address = addresses[0].getAddressLine(0)
                mMap.addMarker(MarkerOptions().position(it).title(address))
            } catch (ex: Exception) {
                ex.printStackTrace()
                showToastMessage(getString(R.string.err_code_1001))
            }

        }
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_get_location -> {
                lastLocation.let {
                    val geoCoder = Geocoder(this, Locale.getDefault())
                    val addresses = geoCoder.getFromLocation(it.latitude, it.longitude, 1);
                    val address = addresses[0].getAddressLine(0)
                    presenter.updateLocation(it.latitude, it.longitude, address)
                }
            }
            R.id.imv_back -> {
                onBackPressed()
                finish()
            }
        }
    }

    private fun requestLocationPermission() {

        if (LocationUtils.isEnableGPS(this)) {
            LocationUtils.requestLocationPermission(this) {
                locationPermissionGranted = true
                getDeviceLocation()
                updateLocationUI()
            }
        } else {
            val mGoogleApiClient = GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build()
            mGoogleApiClient.run {
                connect()
                LocationUtils.requestGPS(this, this@MapsActivity)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true
                    getDeviceLocation()
                    updateLocationUI()
                } else {
                    PrimaryDialog({
                        finish()
                    }, {})
                            .showBtnCancel(false)
                            .setDescription(getString(R.string.GPS_not_found))
                            .show(supportFragmentManager)
                }

            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun updateLocationUI() {
        if (map == null) {
            return
        }
        try {
            if (locationPermissionGranted) {
                mMap.isMyLocationEnabled = true
                mMap.uiSettings.isMyLocationButtonEnabled = true
            } else {
                mMap.isMyLocationEnabled = false
                mMap.uiSettings.isMyLocationButtonEnabled = false
                lastLocation = Location("")
                requestLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e(MapsActivity::class.simpleName, e.message, e)
        }
    }

    private fun getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                val locationResult = fused?.lastLocation
                locationResult?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        lastLocation = task.result
                        lastLocation.let {
                            mMap.clear()
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), DEFAULT_ZOOM.toFloat()))
                            val geoCoder = Geocoder(this, Locale.getDefault())
                            val addresses = geoCoder.getFromLocation(it.latitude, it.longitude, 1);
                            val address = addresses[0].getAddressLine(0)
                            mMap.addMarker(MarkerOptions().position(LatLng(it.latitude, it.longitude)).title(address))
                        }
                    } else {
                        Log.e(MapsActivity::class.simpleName, "Exception: %s", task.exception)
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat()))
                        mMap.uiSettings.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e(MapsActivity::class.simpleName, e.message, e)
        }
    }

    override fun onMyLocationButtonClick(): Boolean {
        getDeviceLocation()
        return true
    }

    override fun showLoading() {
        rll_loading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        rll_loading.visibility = View.GONE
    }

    override fun onBackPress() {}

    override fun showErrorMessage(errMessage: Int) {
        showDialogErrorMessage(getString(errMessage))
    }

    override fun showUpdateLocationSuccess() {
        PrimaryDialog({
            finish()
        }, {})
                .showBtnCancel(false)
                .setDescription(getString(R.string.update_location_success))
                .show(supportFragmentManager)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            LocationUtils.REQUEST_LOCATION -> when (resultCode) {
                RESULT_OK -> {
                    requestLocationPermission()
                }
                else -> {
                    PrimaryDialog({
                        finish()
                    }, {})
                            .showBtnCancel(false)
                            .setDescription(getString(R.string.GPS_not_found))
                            .show(supportFragmentManager)
                }
            }
        }
    }

    override fun onConnected(p0: Bundle?) {
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }


}

