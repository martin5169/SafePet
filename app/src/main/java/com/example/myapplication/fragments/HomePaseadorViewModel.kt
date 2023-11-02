package com.example.myapplication.fragments

import android.annotation.SuppressLint
import android.os.Looper
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.myapplication.repository.PaseadorRepository
import com.example.myapplication.repository.PaseoRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class HomePaseadorViewModel : ViewModel() {

    lateinit var locationCallback: LocationCallback
    val paseoRepository = PaseoRepository()
    val paseadorRepository = PaseadorRepository()

    @SuppressLint("MissingPermission")

    fun startLocationUpdates(location: FusedLocationProviderClient) {

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
}