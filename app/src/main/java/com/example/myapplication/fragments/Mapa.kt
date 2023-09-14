package com.example.myapplication.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.app.ActivityCompat
import com.example.myapplication.R
import com.example.myapplication.entities.Paseador
import com.example.myapplication.entities.UserAbstract
import com.example.myapplication.entities.UserSession
import com.example.myapplication.repository.PaseadorRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Mapa : Fragment() {


    lateinit var locationCallback: LocationCallback
    lateinit var editText: EditText
    lateinit var location: FusedLocationProviderClient
    lateinit var gMap: GoogleMap
    lateinit var database: FirebaseDatabase
    val paseadorRepository = PaseadorRepository.getInstance()
    lateinit var userSession: UserAbstract

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        database = FirebaseDatabase.getInstance()


        createLocationCallback()
        return inflater.inflate(R.layout.fragment_mapa, container, false)
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        }
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        location = LocationServices.getFusedLocationProviderClient(requireContext())
        userSession = UserSession.user
        paseadorRepository.getPaseadores { paseadores ->
            val paseador = paseadores.filter { it.dni == userSession.dni }
            if (!paseador.isNullOrEmpty()) {
                getPaseadoresLocation()
            }
        }


        mapFragment?.getMapAsync() { p0 ->
            gMap = p0
            gMap.isMyLocationEnabled = true
            startLocationUpdates()
        }


    }

    private fun getPaseadoresLocation() {
        val locationRef = database.getReference("paseadores")
        Log.d("DNI", userSession.dni)
        locationRef.orderByChild("dni").equalTo(userSession.dni)
        locationRef.orderByChild("dni").equalTo(userSession.dni).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("UBICACION", (snapshot.children.first().child("password").value.toString()))
                val userLatitude = snapshot.children.first().child("location").child("latitude").value
                val userLongitude = snapshot.children.first().child("location").child("longitude").value
                Log.d("UBICACION", userLongitude.toString() + " " + userLatitude.toString())
                gMap.clear()
                if (userLatitude != null && userLongitude != null) {
                    val userLatLng = LatLng(userLatitude as Double, userLongitude as Double)
                    gMap.addMarker(
                        MarkerOptions()
                            .position(userLatLng)
                            .title("Mi Ubicación")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
                    )
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("UBICACION", "NO SE ENCONTRO AL USUARIO")
            }

        })
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.Builder(5000)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()

        location.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun createLocationCallback() {
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

    private fun updateMarker(location: Location) {
        val currentLatLng = LatLng(location.latitude, location.longitude)
        // Si ya existe un marcador en el mapa, quítalo antes de agregar uno nuevo.

        // Mover la cámara al nuevo marcador.
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 0f), 2000, null)
    }

}