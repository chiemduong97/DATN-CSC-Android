package com.example.client.screens.map.activity

import android.Manifest
import android.annotation.SuppressLint
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
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.client.R
import com.example.client.dialog.PrimaryDialog
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
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMyLocationButtonClickListener, Toolbar.OnMenuItemClickListener, IMapsView {

    private lateinit var mMap: GoogleMap
    private var fused: FusedLocationProviderClient? = null
    private var lastLocation: Location? = null
    private var locationPermissionGranted = false
    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 999
    private val DEFAULT_ZOOM = 12
    private var defaultLocation = LatLng(10.8529727, 106.6295453)

    private val placesClient: PlacesClient? = null
    private val M_MAX_ENTRIES = 5
    private lateinit var likelyPlaceNames: Array<String?>
    private lateinit var likelyPlaceAddresses: Array<String?>
    private lateinit var likelyPlaceAttributions: Array<List<*>?>
    private lateinit var likelyPlaceLatLngs: Array<LatLng?>

    private var present: MapsPresent? = null
    private var dialog: PrimaryDialog? = null
    companion object {
        fun newInstance(from: Context?): Intent {
            return Intent(from, MapsActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        fused = LocationServices.getFusedLocationProviderClient(this)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        present = MapsPresent(this)
        dialog = PrimaryDialog()
        dialog?.getInstance(this)
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
        toolbar.setOnMenuItemClickListener(this)
        imv_back.setOnClickListener(this)
        getLocationPermission()
        if (locationPermissionGranted) {
            updateLocationUI()
            getDeviceLocation()
        }

        mMap.setOnMapClickListener {
            mMap.clear()
            mMap.addMarker(MarkerOptions().position(it).title("It's me"))
            lastLocation?.apply {
                latitude = it.latitude
                longitude = it.longitude
            }
        }
    }


    override fun onClick(v: View?) {
        v?.let {
            when (v.id) {
                R.id.tv_get_location -> {
                    lastLocation?.let {
                        val geocoder = Geocoder(this, Locale.getDefault())
                        val addresses = geocoder.getFromLocation(it.latitude, it.longitude, 1);
                        val address = addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        val city = addresses[0].locality
                        val state = addresses[0].adminArea
                        val country = addresses[0].countryName
                        val postalCode = addresses[0].postalCode
                        val knownName = addresses[0].featureName
                        present?.updateLocation(it.latitude, it.longitude, address)
                    }
                }
                R.id.imv_back -> {
                    onBackPressed()
                    finish()
                }
                else -> {

                }
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

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
        updateLocationUI()
    }

    @SuppressLint("MissingPermission")
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
                lastLocation = null
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    @SuppressLint("MissingPermission")
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
                        lastLocation?.let {
                            mMap.clear()
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), DEFAULT_ZOOM.toFloat()))
                            mMap.addMarker(MarkerOptions().position(LatLng(it.latitude, it.longitude)).title("It's me"))
                        }
                    } else {
                        Log.d("Duong", "Current location is null. Using defaults.")
                        Log.e("Duong", "Exception: %s", task.exception)
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat()))
                        mMap.uiSettings.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    override fun onMyLocationButtonClick(): Boolean {
        getDeviceLocation()
        return true
    }

    @SuppressLint("MissingPermission")
    private fun showCurrentPlace() {
        if (map == null) {
            return
        }
        if (locationPermissionGranted) {

            // Use fields to define the data types to return.
            val placeFields = listOf(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)

            // Use the builder to create a FindCurrentPlaceRequest.
            val request = FindCurrentPlaceRequest.newInstance(placeFields)

            // Get the likely places - that is, the businesses and other points of interest that
            // are the best match for the device's current location.
            val placeResult = placesClient?.findCurrentPlace(request)
            placeResult?.addOnCompleteListener { task ->
                Log.d("Duong", "showCurrentPlace: ")

                if (task.isSuccessful && task.result != null) {
                    val likelyPlaces = task.result

                    // Set the count, handling cases where less than 5 entries are returned.
                    val count = if (likelyPlaces != null && likelyPlaces.placeLikelihoods.size < M_MAX_ENTRIES) {
                        likelyPlaces.placeLikelihoods.size
                    } else {
                        M_MAX_ENTRIES
                    }
                    var i = 0
                    likelyPlaceNames = arrayOfNulls(count)
                    likelyPlaceAddresses = arrayOfNulls(count)
                    likelyPlaceAttributions = arrayOfNulls(count)
                    likelyPlaceLatLngs = arrayOfNulls(count)
                    for (placeLikelihood in likelyPlaces?.placeLikelihoods ?: emptyList()) {
                        // Build a list of likely places to show the user.
                        likelyPlaceNames[i] = placeLikelihood.place.name
                        likelyPlaceAddresses[i] = placeLikelihood.place.address
                        likelyPlaceAttributions[i] = placeLikelihood.place.attributions
                        likelyPlaceLatLngs[i] = placeLikelihood.place.latLng
                        i++
                        if (i > count - 1) {
                            break
                        }
                    }

                    // Show a dialog offering the user the list of likely places, and add a
                    // marker at the selected place.
                    openPlacesDialog()
                } else {
                    Log.e("Duong", "Exception: %s", task.exception)
                }
            }
        } else {
            // The user has not granted permission.
            Log.i("Duong", "The user did not grant location permission.")

            // Add a default marker, because the user hasn't selected a place.
            mMap.addMarker(MarkerOptions()
                    .title(getString(R.string.default_info_title))
                    .position(defaultLocation)
                    .snippet(getString(R.string.default_info_snippet)))

            // Prompt the user for permission.
            getLocationPermission()
        }
    }
    private fun openPlacesDialog() {
        Log.d("Duong", "openPlacesDialog: ")
        // Ask the user to choose the place where they are now.
        val listener = DialogInterface.OnClickListener { _, which -> // The "which" argument contains the position of the selected item.
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

            // Add a marker for the selected place, with an info window
            // showing information about that place.
            mMap.addMarker(MarkerOptions()
                    .title(likelyPlaceNames[which])
                    .position(markerLatLng)
                    .snippet(markerSnippet))

            // Position the map's camera at the location of the marker.
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,
                    DEFAULT_ZOOM.toFloat()))
        }

        // Display the dialog.
        AlertDialog.Builder(this)
                .setTitle(R.string.pick_place)
                .setItems(likelyPlaceNames, listener)
                .show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.option_get_place) {
            showCurrentPlace()
        }
        return true
    }

    override fun showLoading() {
        rll_loading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        rll_loading.visibility = View.GONE
    }

    override fun showErrorMessage(errMessage: Int) {
        dialog?.apply {
            setDescription(getString(errMessage))
            setOKListener {}
            hideBtnCancel()
            show()
        }
    }

    override fun showSuccess() {
        dialog?.apply {
            setDescription(getString(R.string.update_location_success))
            setOKListener {}
            hideBtnCancel()
            show()
        }
    }
}

