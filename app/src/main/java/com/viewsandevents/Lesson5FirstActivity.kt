package com.viewsandevents

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.viewsandevents.databinding.ActivityLesson5FirstBinding
import com.viewsandevents.utils.SharedPreferencesHelper

class Lesson5FirstActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLesson5FirstBinding
    private lateinit var locations: MutableList<Location>
    private lateinit var locationAdapter: LocationAdapter
    private val gson = Gson()
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLesson5FirstBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        sharedPreferencesHelper = SharedPreferencesHelper(this, "locations_pref")

        // Retrieve locations from SharedPreferences
        setLocationAdapter()
        // Add divider
        val dividerItemDecoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        binding.recyclerView.addItemDecoration(dividerItemDecoration)

        binding.addLocationBtn.setOnClickListener {
            val intent = Intent(this, Lesson5SecondActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        setLocationAdapter()
    }

    private fun setLocationAdapter() {
        locations = getLocationsFromSharedPreferences().toMutableList()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        // Set up the adapter with item click and remove bookmark actions
        locationAdapter = LocationAdapter(
            locations,
            onItemClick = { location ->
                handleItemClick(location) // Handle location item click
            },
            onRemoveBookmark = { location ->
                removeBookmark(location)  // Handle remove bookmark click
            }
        )

        binding.recyclerView.adapter = locationAdapter
    }

    private fun getLocationsFromSharedPreferences(): List<Location> {
        val json = sharedPreferencesHelper.getString(Constants.LOCATION_KEY, null)

        return if (json != null) {
            // Convert JSON string to List<Location>
            val type = object : TypeToken<List<Location>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }

    private fun saveLocationsToSharedPreferences(locations: List<Location>) {
        val gson = Gson()

        // Convert list to JSON string
        val json = gson.toJson(locations)
        sharedPreferencesHelper.putString(Constants.LOCATION_KEY, json)
    }

    // Handle location item click
    private fun handleItemClick(location: Location) {
        // Example: Show a Toast or navigate to a details screen
        navigateToDetail(location)
    }

    // Handle remove bookmark action
    private fun removeBookmark(location: Location) {
        // Remove the location from the list
        locations.remove(location)

        // Save the updated list to SharedPreferences
        saveLocationsToSharedPreferences(locations)

        // Notify the adapter of the updated data
        locationAdapter.updateData(locations)
    }

    // Navigate to DetailActivity with location data
    private fun navigateToDetail(location: Location) {
        val intent = Intent(this, Lesson5DetailActivity::class.java).apply {
            putExtra(Constants.LATITUDE, location.latitude)
            putExtra(Constants.LONGITUDE, location.longitude)
        }
        startActivity(intent)
    }
}