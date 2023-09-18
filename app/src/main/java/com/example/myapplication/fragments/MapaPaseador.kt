package com.example.myapplication.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.app.ActivityCompat
import com.example.myapplication.R
import com.example.myapplication.entities.Paseo
import com.example.myapplication.entities.UserAbstract
import com.example.myapplication.entities.UserSession
import com.example.myapplication.repository.PaseadorRepository
import com.example.myapplication.repository.PaseoRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.FirebaseDatabase

class MapaPaseador : Fragment() {


    lateinit var locationCallback: LocationCallback
    lateinit var editText: EditText
    lateinit var location: FusedLocationProviderClient
    lateinit var gMap: GoogleMap
    lateinit var database: FirebaseDatabase
    val paseadorRepository = PaseadorRepository.getInstance()
    val paseoRepository = PaseoRepository.getInstance()
    val mapaViewModel = MapaViewModel()
    lateinit var userSession: UserAbstract
    lateinit var v: View

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        database = FirebaseDatabase.getInstance()

        createLocationCallback()
        v = inflater.inflate(R.layout.fragment_mapa_paseador, container, false)
        return v
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

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync() { p0 ->
            gMap = p0
            gMap.isMyLocationEnabled = true
            startLocationUpdates()
        }

        location = LocationServices.getFusedLocationProviderClient(requireContext())
        userSession = UserSession.user

        paseoRepository.getPaseo(userSession.dni).addOnCompleteListener {
            val paseo = it.result?.getValue(Paseo::class.java)
            if (paseo == null) {
                getLocation()
            }else{
                mapaViewModel.addMarcador(gMap, LatLng(paseo.paseador.location.latitude, paseo.paseador.location.longitude), paseo.paseador.dni)
                      }
        }

        //paseoRepository.addPaseo(Paseo(Paseador(), userSession as User))
    }

    private fun getLocation() {
        paseadorRepository.getPaseadores { paseadores ->
            gMap.clear()
            val paseando = paseadores.filter { it.estaPaseando }
            paseando.forEach {
                mapaViewModel.getPaseadoresLocation(gMap, it);
            }
        }
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
    }

}