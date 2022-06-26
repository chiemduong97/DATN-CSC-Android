package com.example.client.screens.map.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.client.R
import com.example.client.base.BaseActivityMVP
import com.example.client.dialog.PrimaryDialog
import com.example.client.screens.map.present.IMapsPresent
import com.example.client.screens.map.present.MapsPresent
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.android.synthetic.main.activity_maps.*
import java.lang.Exception
import java.util.*


class MapsActivity : BaseActivityMVP<IMapsPresent>(), OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMyLocationButtonClickListener, IMapsView {

    private lateinit var mMap: GoogleMap
    private var fused: FusedLocationProviderClient? = null
    private var lastLocation = Location("")
    private var locationPermissionGranted = false
    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION by lazy { 999 }
    private val DEFAULT_ZOOM by lazy { 12 }
    private var defaultLocation = LatLng(10.8529727, 106.6295453)

    private val placesClient: PlacesClient? = null
    private val M_MAX_ENTRIES by lazy { 5 }
    private lateinit var likelyPlaceNames: Array<String?>
    private lateinit var likelyPlaceAddresses: Array<String?>
    private lateinit var likelyPlaceAttributions: Array<List<*>?>
    private lateinit var likelyPlaceLatLngs: Array<LatLng?>

    companion object {
        fun newInstance(from: Activity): Intent {
            return Intent(from, MapsActivity::class.java)
        }
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
        getLocationPermission()
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
//                        val city = addresses[0].locality
//                        val state = addresses[0].adminArea
//                        val country = addresses[0].countryName
//                        val postalCode = addresses[0].postalCode
//                        val knownName = addresses[0].featureName
                    presenter.updateLocation(it.latitude, it.longitude, address)
                }
            }
            R.id.imv_back -> {
                onBackPressed()
                finish()
            }
        }
    }

    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        locationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true
                    getDeviceLocation()
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
        updateLocationUI()
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
                getLocationPermission()
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

//    @SuppressLint("MissingPermission")
//    private fun showCurrentPlace() {
//        if (map == null) {
//            return
//        }
//        if (locationPermissionGranted) {
//            val placeFields = listOf(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
//            val request = FindCurrentPlaceRequest.newInstance(placeFields)
//            val placeResult = placesClient?.findCurrentPlace(request)
//            placeResult?.addOnCompleteListener { task ->
//                if (task.isSuccessful && task.result != null) {
//                    val likelyPlaces = task.result
//                    val count = if (likelyPlaces != null && likelyPlaces.placeLikelihoods.size < M_MAX_ENTRIES) {
//                        likelyPlaces.placeLikelihoods.size
//                    } else {
//                        M_MAX_ENTRIES
//                    }
//                    var i = 0
//                    likelyPlaceNames = arrayOfNulls(count)
//                    likelyPlaceAddresses = arrayOfNulls(count)
//                    likelyPlaceAttributions = arrayOfNulls(count)
//                    likelyPlaceLatLngs = arrayOfNulls(count)
//                    for (placeLikelihood in likelyPlaces?.placeLikelihoods ?: emptyList()) {
//                        likelyPlaceNames[i] = placeLikelihood.place.name
//                        likelyPlaceAddresses[i] = placeLikelihood.place.address
//                        likelyPlaceAttributions[i] = placeLikelihood.place.attributions
//                        likelyPlaceLatLngs[i] = placeLikelihood.place.latLng
//                        i++
//                        if (i > count - 1) {
//                            break
//                        }
//                    }
//                    openPlacesDialog()
//                } else {
//                    Log.e(MapsActivity::class.simpleName, "Exception: %s", task.exception)
//                }
//            }
//        } else {
//            Log.i(MapsActivity::class.simpleName, "The user did not grant location permission.")
//            mMap.addMarker(MarkerOptions()
//                    .title(getString(R.string.default_info_title))
//                    .position(defaultLocation)
//                    .snippet(getString(R.string.default_info_snippet)))
//            getLocationPermission()
//        }
//    }

    private fun openPlacesDialog() {
        val listener = DialogInterface.OnClickListener { _, which ->
            val markerLatLng = likelyPlaceLatLngs[which]
            var markerSnippet = likelyPlaceAddresses[which]
            if (likelyPlaceAttributions[which] != null) {
                markerSnippet = """
                $markerSnippet
                ${likelyPlaceAttributions[which]}
                """.trimIndent()
            }

            if (markerLatLng == null) {
                return@OnClickListener
            }

            mMap.addMarker(MarkerOptions()
                    .title(likelyPlaceNames[which])
                    .position(markerLatLng)
                    .snippet(markerSnippet))

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,
                    DEFAULT_ZOOM.toFloat()))
        }

        AlertDialog.Builder(this)
                .setTitle(R.string.pick_place)
                .setItems(likelyPlaceNames, listener)
                .show()
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


}

