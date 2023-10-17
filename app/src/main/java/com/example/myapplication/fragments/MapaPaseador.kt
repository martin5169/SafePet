package com.example.myapplication.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.entities.Paseo
import com.example.myapplication.entities.UserAbstract
import com.example.myapplication.entities.UserSession
import com.example.myapplication.repository.PaseadorRepository
import com.example.myapplication.repository.PaseoRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.FirebaseDatabase

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
                Snackbar.make(v, "Tiene un paseo asignado", Snackbar.LENGTH_SHORT).show()
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