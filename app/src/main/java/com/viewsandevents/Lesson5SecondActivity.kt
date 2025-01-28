package com.viewsandevents

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.viewsandevents.databinding.ActivityLesson5SecondBinding
import com.viewsandevents.utils.SharedPreferencesHelper

class Lesson5SecondActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityLesson5SecondBinding
    private var googleMap: GoogleMap? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentMarker: Marker? = null // Reference to the marker
    private val gson = Gson()
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLesson5SecondBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        sharedPreferencesHelper = SharedPreferencesHelper(this, Constants.LOCATION_PREF)
        // Initialize the map with a Bundle
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(Constants.MAPVIEW_BUNDLE_KEY)
        }
        binding.mapView.onCreate(mapViewBundle)

        // Set the map ready callback
        binding.mapView.getMapAsync(this)

        // Handle button click to save the location
        binding.addLocationBtn.setOnClickListener {
            saveCurrentLocation()
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        enableUserLocation()

        // Set a click listener on the map
        googleMap?.setOnMapClickListener { latLng ->
            updateMarker(latLng) // Update the marker position on map click
        }
    }

    private fun enableUserLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap?.isMyLocationEnabled = true // Show the blue dot for current location
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                    updateMarker(currentLatLng) // Add the marker at the user's current location
                }
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                Constants.LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun updateMarker(latLng: LatLng) {
        if (currentMarker == null) {
            // Add a new marker if it doesn't exist
            currentMarker = googleMap?.addMarker(
                MarkerOptions().position(latLng).title(getString(R.string.marker))
            )
        } else {
            // Update the marker's position if it already exists
            currentMarker?.position = latLng
        }
    }

    private fun saveCurrentLocation() {
        val markerPosition = currentMarker?.position
        if (markerPosition != null) {
            // Get the saved locations list
            val locationsList = getSavedLocations()

            // Add the current marker's position to the list
            val newLocation = mapOf(
                Constants.LATITUDE to markerPosition.latitude,
                Constants.LONGITUDE to markerPosition.longitude
            )
            locationsList.add(newLocation)

            // Save the updated list to SharedPreferences
            saveLocationsToSharedPreferences(locationsList)
            Toast.makeText(this, "Location Saved: ${markerPosition.latitude}, ${markerPosition.longitude}", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "No marker selected to save.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getSavedLocations(): MutableList<Map<String, Double>> {
        val json = sharedPreferencesHelper.getString(Constants.LOCATION_KEY, null)
        return if (json != null) {
            val type = object : TypeToken<List<Location>>() {}.type
            gson.fromJson(json, type)
        } else {
            mutableListOf()
        }
    }

    private fun saveLocationsToSharedPreferences(locations: List<Map<String, Double>>) {
        val json = gson.toJson(locations) // Convert the list to a JSON string
        sharedPreferencesHelper.putString(Constants.LOCATION_KEY, json) // Save to SharedPreferences
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.LOCATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                enableUserLocation()
            }
        }
    }


    // Forward lifecycle methods to MapView
    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onPause() {
        binding.mapView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        binding.mapView.onDestroy()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        var mapViewBundle = outState.getBundle(Constants.MAPVIEW_BUNDLE_KEY)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(Constants.MAPVIEW_BUNDLE_KEY, mapViewBundle)
        }
        binding.mapView.onSaveInstanceState(mapViewBundle)
    }
}