package com.viewsandevents

import WeatherApiService
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.viewsandevents.databinding.ActivityLesson5DetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

class Lesson5DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLesson5DetailBinding
    private var latitude by Delegates.notNull<Double>()
    private var longitude by Delegates.notNull<Double>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLesson5DetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Get the passed location data from the intent
        latitude = intent.getDoubleExtra(Constants.LATITUDE, 0.0)
        longitude = intent.getDoubleExtra(Constants.LONGITUDE, 0.0)

        // Fetch weather data
        fetchWeatherData(latitude, longitude)
    }

    private fun fetchWeatherData(latitude: Double, longitude: Double) {
        binding.progressBar.visibility = View.VISIBLE
        val service = RetrofitClient.retrofit.create(WeatherApiService::class.java)

        val call = service.getWeather(latitude, longitude, Constants.API_KEY)
        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { weatherResponse ->
                        val temperature = "${kelvinToCelsius(weatherResponse.main.temp)}°C"
                        binding.progressBar.visibility = View.GONE
                        // Update UI with weather data
                        binding.latitudeValue.text = weatherResponse.coord.lat.toString()
                        binding.longitudeValue.text = weatherResponse.coord.lon.toString()
                        binding.weatherDescription.text = weatherResponse.weather[0].description
                        binding.temperature.text = temperature
                        binding.country.text = weatherResponse.sys.country
                        binding.city.text = weatherResponse.name
                    }
                } else {
                    Toast.makeText(this@Lesson5DetailActivity, "Failed to fetch weather data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Toast.makeText(this@Lesson5DetailActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun kelvinToCelsius(temp: Double): String {
        return String.format("%.2f", temp - 273.15)
    }
}