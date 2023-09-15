package com.example.myapplication.fragments

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.myapplication.R
import com.example.myapplication.entities.Paseador
import com.example.myapplication.repository.PaseadorRepository
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
    private val paseadorRepository: PaseadorRepository = PaseadorRepository()
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
                Log.d("UBICACION", userLongitude.toString() + " " + userLatitude.toString())
                if (userLatitude != null && userLongitude != null) {
                    val userLatLng = LatLng(userLatitude as Double, userLongitude as Double)

                    val marcador = marcadores.find { it?.title.equals(paseador.dni) }
                    if (marcador != null) {
                        marcador.position =
                            LatLng(userLatitude.toDouble(), userLongitude.toDouble())
                    } else {
                        addMarcador(gMap, LatLng(userLatitude, userLongitude), paseador.dni)
                    }

                    //gMap.setOnInfoWindowClickListener {}
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("UBICACION", "NO SE ENCONTRO AL USUARIO")
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