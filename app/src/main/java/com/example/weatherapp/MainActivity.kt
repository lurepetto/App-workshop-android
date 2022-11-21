package com.example.weatherapp

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.json.JSONObject
import com.android.volley.Response

class MainActivity : AppCompatActivity() {

    // URL para obtener el JSON
    var weather_url1 = ""

    // key de API para la URL
    var api_key1 = "b2f751bf54744fc0b430f20d13e2eb4b"

    private lateinit var textView: TextView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var btnVar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // asignacion de widget a variable
        btnVar = findViewById(R.id.btVar1)

        // Creamos una instancia fusionada
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        Log.e("lat", weather_url1)

        // Funcion asincrona para llamar
        // las coordenadas con el boton
        btnVar.setOnClickListener {
            Log.e("lat", "onClick")
            obtainLocation()
        }


    }

    @SuppressLint("MissingPermission")
    private fun obtainLocation() {
        Log.e("lat", "function")
        // obtener la última ubicación
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                weather_url1 = "https://api.weatherbit.io/v2.0/current?" + "lat=" + location?.latitude + "&lon=" + location?.longitude + "&key=" + api_key1
                Log.e("lat", weather_url1.toString())
                getTemp()
            }
    }

    fun getTemp() {
        // Instanciar la cola de requests
        val queue = Volley.newRequestQueue(this)
        val url: String = weather_url1
        Log.e("lat", url)

        val stringRequest = StringRequest(Request.Method.GET, url, Response.Listener<String>{ response ->
            Log.e("lat", response.toString())

            // obtener el objeto JSON
            val obj = JSONObject(response)

            // obtener el arreglo del objeto con nombre "data"
            val arr = obj.getJSONArray("data")
            Log.e("lat obj1", arr.toString())

            // obtener el objeto JSON de la posicion 0
            // en el arreglo
            val obj2 = arr.getJSONObject(0)
            Log.e("lat obj2", obj2.toString())

            // establecer temperatura y ciudad
            textView.text = obj2.getString("temp") + "deg Celsius in " + obj2.getString("city_name")
        },
            Response.ErrorListener { textView.text = "No funcionó" })
        queue.add(stringRequest)
    }
}