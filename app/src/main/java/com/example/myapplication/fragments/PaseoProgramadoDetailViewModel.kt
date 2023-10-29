package com.example.myapplication.fragments

import android.annotation.SuppressLint
import android.os.Looper
import android.util.Log

import androidx.lifecycle.ViewModel
import com.example.myapplication.entities.EstadoEnum
import com.example.myapplication.entities.UserAbstract
import com.example.myapplication.repository.PaseadorRepository
import com.example.myapplication.repository.PaseoRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.google.android.gms.wallet.PaymentsClient
import org.json.JSONArray
import org.json.JSONObject

class PaseoProgramadoDetailViewModel : ViewModel() {

    lateinit var location: FusedLocationProviderClient
    lateinit var locationCallback: LocationCallback
    val paseoRepository = PaseoRepository()
    val paseadorRepository = PaseadorRepository()
    private lateinit var paymentsClient: PaymentsClient
    private lateinit var garmentList: JSONArray
    private lateinit var selectedGarment: JSONObject
    private val LOAD_PAYMENT_DATA_REQUEST_CODE = 991

    @SuppressLint("MissingPermission")
     fun startLocationUpdates() {
        val locationRequest = LocationRequest.Builder(2000)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()

        location.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

     fun createLocationCallback(dniPaseador: String) {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.lastLocation?.let { location ->
                    Log.d("LOCATION", "${location.latitude.toString()} ${location.longitude.toString()}")
                    paseoRepository.updateLocationPaseador(dniPaseador, location.latitude, location.longitude)
                    paseadorRepository.updateLocationPaseador(dniPaseador, location.latitude, location.longitude)
                }
            }
        }
    }

    fun createPaseoActivo(location: FusedLocationProviderClient, user: UserAbstract, id: String) {
        this.location = location
        createLocationCallback(user.dni)
        startLocationUpdates()
        paseoRepository.updateEstadoPaseo(id, EstadoEnum.ACTIVO)

    }

   fun realizarPago() {

   }
}