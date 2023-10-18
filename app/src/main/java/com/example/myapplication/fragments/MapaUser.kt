package com.example.myapplication.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.app.ActivityCompat
import com.example.myapplication.R
import com.example.myapplication.entities.PaseoProgramado
import com.example.myapplication.entities.UserAbstract
import com.example.myapplication.entities.UserSession
import com.example.myapplication.repository.PaseadorRepository
import com.example.myapplication.repository.PaseoRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.firebase.database.FirebaseDatabase

class MapaUser : Fragment() {


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
        v = inflater.inflate(R.layout.fragment_mapa_duenio, container, false)
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

        val mapFragment = childFragmentManager.findFragmentById(R.id.fragmentSeleccionarUbicacion) as SupportMapFragment
        mapFragment.getMapAsync() { p0 ->
            gMap = p0
            gMap.isMyLocationEnabled = true
        }

        location = LocationServices.getFusedLocationProviderClient(requireContext())
        userSession = UserSession.user

        paseoRepository.getPaseoUser(userSession.dni).addOnCompleteListener {
            val paseo = it.result?.getValue(PaseoProgramado::class.java)
            if (paseo == null) {
                getLocation()
            }else{
                mapaViewModel.getPaseoUser(gMap, it.result!!)
            }
        }
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
}