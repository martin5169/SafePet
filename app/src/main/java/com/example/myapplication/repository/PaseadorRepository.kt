package com.example.myapplication.repository

import com.example.myapplication.entities.Paseador
import com.google.firebase.database.*

class PaseadorRepository() {
  private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
  private val paseadoresReference: DatabaseReference = database.getReference("paseadores")

  companion object {
    @Volatile
    private var instance: PaseadorRepository? = null

    fun getInstance(): PaseadorRepository {
      return instance ?: synchronized(this) {
        instance ?: PaseadorRepository().also { instance = it }
      }
    }
  }

  // Agregar un paseador a la base de datos
  fun addUser(paseador: Paseador) {
    val paseadorKey = paseadoresReference.push().key // Generar una clave única para el paseador
    paseadorKey?.let {
      paseadoresReference.child(it).setValue(paseador)
    }
  }

  // Recuperar la lista de usuarios desde la base de datos
  fun getPaseadores(callback: (List<Paseador>) -> Unit) {
    paseadoresReference.addListenerForSingleValueEvent(object : ValueEventListener {
      override fun onDataChange(snapshot: DataSnapshot) {
        val paseadoresList = mutableListOf<Paseador>()
        for (childSnapshot in snapshot.children) {
          val paseador = childSnapshot.getValue(Paseador::class.java)
          paseador?.let {
            paseadoresList.add(it)
          }
        }
        callback(paseadoresList)
      }

      override fun onCancelled(error: DatabaseError) {

      }
    })
  }

  fun updatePaseador(userDni: String, newMail: String, newPassword: String) {
    val usersQuery = paseadoresReference.orderByChild("dni").equalTo(userDni)

    usersQuery.addListenerForSingleValueEvent(object : ValueEventListener {
      override fun onDataChange(snapshot: DataSnapshot) {
        if (snapshot.exists()) {
          for (userSnapshot in snapshot.children) {

            userSnapshot.ref.child("mail").setValue(newMail)
            userSnapshot.ref.child("password").setValue(newPassword)
          }
        }
      }

      override fun onCancelled(error: DatabaseError) {

      }
    })
  }

}

