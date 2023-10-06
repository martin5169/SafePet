package com.example.myapplication.repository

import android.util.Log
import com.example.myapplication.entities.Paseador
import com.example.myapplication.entities.Paseo
import com.example.myapplication.entities.PaseoProgramado
import com.example.myapplication.entities.User
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PaseoProgramadoRepository() {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val paseosReference: DatabaseReference = database.getReference("paseosProgramados")

    companion object {
        @Volatile
        private var instance: PaseoProgramadoRepository? = null

        fun getInstance(): PaseoProgramadoRepository {
            return instance ?: synchronized(this) {
                instance ?: PaseoProgramadoRepository().also { instance = it }
            }
        }
    }


    fun addPaseo(paseo: PaseoProgramado) {
        val userKey = paseosReference.push().key // Generar una clave única para el paseo
        userKey?.let {
            paseo.id = it // Asignar la clave única al atributo "id" del paseo programado
            paseosReference.child(it).setValue(paseo)
        }
    }
    fun deletePaseo(paseo: PaseoProgramado) {
        paseosReference.child(paseo.id).removeValue()
    }

    fun getPaseos(callback: (List<PaseoProgramado>) -> Unit) {
        paseosReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val paseosList = mutableListOf<PaseoProgramado>()
                for (childSnapshot in snapshot.children) {
                    val paseoProgramado = childSnapshot.getValue(PaseoProgramado::class.java)
                    paseoProgramado?.let {
                        paseosList.add(it)
                    }
                }
                callback(paseosList)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


}