package com.example.myapplication.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.entities.EstadoEnum
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
import com.google.android.gms.maps.model.Marker
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
        v = inflater.inflate(R.layout.fragment_mapa_paseador, container, false)
        mapaViewModel.createLocationCallback()
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

        paseoRepository.getPaseosPaseador(userSession.dni) {
            if (it.isNullOrEmpty()) {
                Snackbar.make(v, "No tiene un paseo asignado", Snackbar.LENGTH_SHORT).show()
            } else {
                getUsersLocation(it)
            }
        }


        //paseoRepository.addPaseo(Paseo(Paseador(), userSession as User))
    }

    private fun getUsersLocation(paseos: List<PaseoProgramado>) {
        paseos.forEach {
            if(it.estado == EstadoEnum.ACTIVO) {
                Snackbar.make(v, "Tiene un paseo asignado", Snackbar.LENGTH_SHORT).show()
                mapaViewModel.getUsersLocation(gMap, it.user);
            }
        }
    }

}