package com.example.myapplication.fragments

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.myapplication.R
import com.example.myapplication.entities.Paseador
import com.example.myapplication.entities.PaseoProgramado
import com.example.myapplication.repository.PaseadorRepository
import com.example.myapplication.repository.PaseoRepository
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MapaViewModel : ViewModel() {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val paseoRepository = PaseoRepository.getInstance()
    val marcadores: MutableList<Marker?> = mutableListOf()
    fun getPaseadoresLocation(gMap: GoogleMap, paseador: Paseador) {
        val locationRef = database.getReference("paseadores")

        locationRef.orderByChild("dni").equalTo(paseador.dni).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userLatitude =
                    snapshot.children.first().child("location").child("latitude").value
                val userLongitude =
                    snapshot.children.first().child("location").child("longitude").value

                if (userLatitude != null && userLongitude != null) {
                    val userLatLng = LatLng(userLatitude as Double, userLongitude as Double)

                    val marcador = marcadores.find { it?.title.equals(paseador.dni) }
                    if (marcador != null) {
                        marcador.position =
                            LatLng(userLatitude.toDouble(), userLongitude.toDouble())
                    } else {
                        addMarcador(gMap, LatLng(userLatitude, userLongitude), paseador.dni)

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("UBICACION", "NO SE ENCONTRO AL USUARIO")
            }

        })
    }

    fun getPaseoUser(gMap: GoogleMap,paseo: PaseoProgramado) {
        val dniUser = paseo.user.dni

        paseoRepository.getPaseoUserRef(paseo.id).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val mapDataSnap = snapshot.value as HashMap<*, *>
                val mapPaseoSnap = mapDataSnap.get(mapDataSnap.keys.first()) as HashMap<*, *>
                val paseadorMap = mapPaseoSnap.get("paseador") as HashMap<*, *>
                val paseadorDni = paseadorMap.get("dni").toString()
                val locationMap = paseadorMap.get("location") as HashMap<*, *>
                val latitude = locationMap.get("latitude").toString().toDouble()
                val longitude = locationMap.get("longitude").toString().toDouble()
                Log.d("PASO POR ACA", "PASO POR ACA")
                if (latitude != null && longitude != null) {
                    val marcador = marcadores.find { it?.title.equals(paseadorDni) }
                    if (marcador != null) {
                        Log.d("PASO POR ACA", "PASO POR $latitude $longitude")
                        marcador.position = LatLng(latitude, longitude)
                    } else {
                        addMarcador(gMap, LatLng(latitude, longitude), paseadorDni)
                        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitude as Double, longitude as Double), 15f))
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    fun addMarcador(gMap: GoogleMap, latLng: LatLng, dni: String) {
        val marker = gMap.addMarker(
            MarkerOptions()
                .position(latLng)
                .title(dni)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mappeticon))
        )
        marcadores.add(marker)
    }
}