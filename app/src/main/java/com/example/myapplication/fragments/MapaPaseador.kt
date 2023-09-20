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
import com.example.myapplication.entities.Paseo
import com.example.myapplication.entities.User
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
import com.google.firebase.database.GenericTypeIndicator
import java.util.Objects

class MapaPaseador : Fragment() {


    lateinit var locationCallback: LocationCallback
    lateinit var location: FusedLocationProviderClient
    lateinit var gMap: GoogleMap
    lateinit var database: FirebaseDatabase
    val paseadorRepository = PaseadorRepository.getInstance()
    val paseoRepository = PaseoRepository.getInstance()
    val mapaViewModel = MapaPaseadorViewModel()
    lateinit var userSession: UserAbstract
    lateinit var v: View

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        database = FirebaseDatabase.getInstance()

        mapaViewModel.createLocationCallback()
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
        location = LocationServices.getFusedLocationProviderClient(requireContext())
        userSession = UserSession.user

        val mapFragment = childFragmentManager.findFragmentById(R.id.map2) as SupportMapFragment
        mapFragment.getMapAsync() { p0 ->
            gMap = p0
            gMap.isMyLocationEnabled = true
            mapaViewModel.startLocationUpdates(location)
        }



        paseoRepository.getPaseosPaseador(userSession.dni) {
            if (it.isNullOrEmpty()) {
                Snackbar.make(v, "No tiene un paseo asignado", Snackbar.LENGTH_SHORT).show()
            } else {
                getUsersLocation(it)
            }
        }

        //paseoRepository.addPaseo(Paseo(Paseador(), userSession as User))
    }

    private fun getUsersLocation(paseos: List<Paseo>) {
        paseos.forEach {
            Log.d("USER 123", it.user.toString())
            mapaViewModel.getUsersLocation(gMap, it.user);
        }
    }

}