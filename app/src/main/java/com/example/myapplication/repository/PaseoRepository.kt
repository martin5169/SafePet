package com.example.myapplication.repository

import android.util.Log
import com.example.myapplication.entities.EstadoEnum
import com.example.myapplication.entities.Paseador
import com.example.myapplication.entities.PaseoProgramado
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class PaseoRepository {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val paseosReference: DatabaseReference = database.getReference("paseosProgramados")

    companion object {
        @Volatile
        private var instance: PaseoRepository? = null

        fun getInstance(): PaseoRepository {
            return instance ?: synchronized(this) {
                instance ?: PaseoRepository().also { instance = it }
            }
        }
    }

    fun getPaseoUser(dniUser: String): Task<DataSnapshot?> {
       return paseosReference.orderByChild("user/dni").equalTo(dniUser).get()
    }

    fun getPaseoUserRef(dniUser: String): Query {
        return paseosReference.orderByChild("user/dni").equalTo(dniUser)
    }

    fun getPaseosPaseador(dniPaseador: String, callback: (List<PaseoProgramado>) -> Unit) {
        paseosReference.orderByChild("paseador/dni").equalTo(dniPaseador).get().addOnCompleteListener {
            val paseosList = mutableListOf<PaseoProgramado>()
            for (childSnapshot in it.result.children) {
                val paseo = childSnapshot.getValue(PaseoProgramado::class.java)
                paseosList.add(paseo!!)
            }
            callback(paseosList)
        }
    }

    fun addPaseo(paseo: PaseoProgramado): Task<Void> {
        val paseadorKey = paseosReference.push().key // erar una clave única para el paseador

        return paseosReference.child(paseadorKey!!).setValue(paseo)
    }

    fun updateLocationPaseador(paseadorDni: String, latitude: Double, longitude: Double) {
        val usersQuery = paseosReference.orderByChild("paseador/dni").equalTo(paseadorDni)

        usersQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        userSnapshot.ref.child("paseador").child("location").child("latitude").setValue(latitude)
                        userSnapshot.ref.child("paseador").child("location").child("longitude").setValue(longitude)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun updateEstadoPaseo(id: String, estado: EstadoEnum) {
        val usersQuery = paseosReference.child(id)
        Log.d("CLICK", "CLICK")
        usersQuery.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    snapshot.ref.child("estado").setValue(estado)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
    fun updateCalificacionPaseo(id: String, calificacion: Int) {
        val usersQuery = paseosReference.child(id)
        usersQuery.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    snapshot.ref.child("calificacion").setValue(calificacion)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

}