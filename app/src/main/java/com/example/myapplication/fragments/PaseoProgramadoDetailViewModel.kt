package com.example.myapplication.fragments

import android.annotation.SuppressLint
import android.os.Looper

import androidx.lifecycle.ViewModel
import com.example.myapplication.entities.Paseo
import com.example.myapplication.entities.PaseoProgramado
import com.example.myapplication.entities.UserAbstract
import com.example.myapplication.repository.PaseadorRepository
import com.example.myapplication.repository.PaseoRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng

class PaseoProgramadoDetailViewModel : ViewModel() {

    lateinit var location: FusedLocationProviderClient
    lateinit var locationCallback: LocationCallback
    val paseoRepository = PaseoRepository()
    val paseadorRepository = PaseadorRepository()

    @SuppressLint("MissingPermission")
     fun startLocationUpdates() {
        val locationRequest = LocationRequest.Builder(5000)
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
                    paseoRepository.updateLocationPaseador(dniPaseador, location.latitude, location.longitude)
                    paseadorRepository.updateLocationPaseador(dniPaseador, location.latitude, location.longitude)
                }
            }
        }
    }

    fun createPaseoActivo(location: FusedLocationProviderClient, paseo: PaseoProgramado, user: UserAbstract) {
        this.location = location
        val paseoActivo = Paseo(paseo.paseador, paseo.user)
        paseoRepository.addPaseo(paseoActivo).addOnCompleteListener() {
            createLocationCallback(user.dni)
            startLocationUpdates()
        }
    }
}