package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.weatherapp.databinding.ActivityMainBinding
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    //cityName carries the country name, state code, and country code.
    private val cityName = "nigeria, ng"
    private val key = "47e7d224d2a6b5b5d99d9f01331db29f"
    private var url = "https://api.openweathermap.org/data/2.5/weather?q=" +
            "$cityName&appid=$key"

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        whetherApi()
        binding.refresh.setOnRefreshListener {
            binding.refresh.isRefreshing = true
            whetherApi()
            binding.refresh.isRefreshing = false
        }
    }

    private fun whetherApi(){

        val queue = Volley.newRequestQueue(this)

        binding.loader.visibility = View.VISIBLE
        binding.viewContainer.visibility = View.GONE

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            //Adding an action to the response.
            { response ->
                try {
                    val jsonObj = JSONObject(response)
                    val main = jsonObj.getJSONObject("main")
                    val temp = main.getString("temp") + "°C"
                    val name = jsonObj.getString("name")
                    val tempMin = main.getString("temp_min")
                    val tempMax = main.getString("temp_max")
                    val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
                    val description = weather.getString("description")
                    val updated = SimpleDateFormat("dd/MM/yyyy  hh:mm a", Locale.getDefault()).format(Date())

                    binding.temp.text = temp
                    binding.Location.text = name
                    binding.minTemp.text = tempMin
                    binding.maxTemp.text = tempMax
                    binding.clearSky.text = description.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(
                            Locale.ROOT
                        ) else it.toString()
                    }
                    binding.updated.text = updated
                    binding.loader.visibility = View.GONE
                    binding.errorMessage.visibility = View.GONE
                    binding.viewContainer.visibility = View.VISIBLE

                    Log.e("Response", "Response : $response")
                    Toast.makeText(this, "Successful ", Toast.LENGTH_LONG).show()
                    }
                catch (e: Exception) {
                    binding.loader.visibility = View.GONE
                    binding.errorMessage.visibility = View.VISIBLE

                    Toast.makeText(this, "$e", Toast.LENGTH_LONG).show()
                }

            },
            { error ->
                Toast.makeText(this, "$error", Toast.LENGTH_LONG).show()
            })
        queue.add(stringRequest)
    }
}