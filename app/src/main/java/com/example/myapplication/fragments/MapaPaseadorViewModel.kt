package com.example.myapplication.fragments

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.myapplication.R
import com.example.myapplication.entities.User
import com.example.myapplication.repository.PaseadorRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MapaPaseadorViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    val paseadorRepository = PaseadorRepository.getInstance()
    lateinit var locationCallback: LocationCallback
    val database = FirebaseDatabase.getInstance()
    val marcadores: MutableList<Marker?> = mutableListOf()
    @SuppressLint("MissingPermission")
    fun startLocationUpdates(location: FusedLocationProviderClient) {
        val locationRequest = LocationRequest.Builder(5000)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()

        location.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    fun createLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.lastLocation?.let { location ->
                    // Actualizar el marcador en el mapa con la nueva ubicación.
                    val locationRef = database.reference.child("users").child("1").child("location")

                    locationRef.setValue(com.example.myapplication.entities.Location(location.latitude, location.longitude))
                    updateMarker(location)
                }
            }
        }
    }

    fun getUsersLocation(gMap: GoogleMap, user: User) {
        val locationRef = database.getReference("users")
        Log.d("USER", user.dni)
        locationRef.orderByChild("dni").equalTo(user.dni).get().addOnCompleteListener {
            Log.d("PASEOS USERS", it.result.toString())
        }
        locationRef.orderByChild("dni").equalTo(user.dni).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("PASEOS USER", snapshot.value.toString())
                val userLatitude =
                    snapshot.children.first().child("location").child("latitude").value
                val userLongitude =
                    snapshot.children.first().child("location").child("longitude").value
                Log.d("UBICACION", userLongitude.toString() + " " + userLatitude.toString())
                if (userLatitude != null && userLongitude != null) {
                    val userLatLng = LatLng(userLatitude as Double, userLongitude as Double)

                    val marcador = marcadores.find { it?.title.equals(user.dni) }
                    if (marcador != null) {
                        marcador.position =
                            LatLng(userLatitude.toDouble(), userLongitude.toDouble())
                    } else {
                        addMarcador(gMap, LatLng(userLatitude, userLongitude), user.dni)
                    }

                    //gMap.setOnInfoWindowClickListener {}
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("UBICACION", "NO SE ENCONTRO AL USUARIO")
            }

        })
    }
    private fun updateMarker(location: Location) {
        val currentLatLng = LatLng(location.latitude, location.longitude)
        // Si ya existe un marcador en el mapa, quítalo antes de agregar uno nuevo.

        // Mover la cámara al nuevo marcador.
    }

    fun addMarcador(gMap: GoogleMap, latLng: LatLng, dni: String) {
        val marker = gMap.addMarker(
            MarkerOptions()
                .position(latLng)
                .title(dni)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icons8person30))
        )
        marcadores.add(marker)
    }
}