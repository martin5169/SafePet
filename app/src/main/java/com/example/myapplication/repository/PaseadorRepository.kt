package com.example.myapplication.repository

import com.example.myapplication.entities.Paseador
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

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

  fun updateLocationPaseador(paseadorDni: String, latitude: Double, longitude: Double) {
    val usersQuery = paseadoresReference.orderByChild("dni").equalTo(paseadorDni)

    usersQuery.addListenerForSingleValueEvent(object : ValueEventListener {
      override fun onDataChange(snapshot: DataSnapshot) {
        if (snapshot.exists()) {
          for (userSnapshot in snapshot.children) {
            userSnapshot.ref.child("estaPaseando").setValue(true)
            userSnapshot.ref.child("location").child("latitude").setValue(latitude)
            userSnapshot.ref.child("location").child("longitude").setValue(longitude)
          }
        }
      }

      override fun onCancelled(error: DatabaseError) {

      }
    })
  }


  fun updateTarifa(userDni: String, newTarifa: String) {
    val usersQuery = paseadoresReference.orderByChild("dni").equalTo(userDni)

    usersQuery.addListenerForSingleValueEvent(object : ValueEventListener {
      override fun onDataChange(snapshot: DataSnapshot) {
        if (snapshot.exists()) {
          for (userSnapshot in snapshot.children) {

            userSnapshot.ref.child("tarifa").setValue(newTarifa.toInt())

          }
        }
      }

      override fun onCancelled(error: DatabaseError) {

      }
    })
  }

  fun calificar(userDni: String, calificacion: Int) {
    val usersQuery = paseadoresReference.orderByChild("dni").equalTo(userDni)

    usersQuery.addListenerForSingleValueEvent(object : ValueEventListener {
      override fun onDataChange(snapshot: DataSnapshot) {
        if (snapshot.exists()) {
          for (userSnapshot in snapshot.children) {
            val paseador = userSnapshot.getValue(Paseador::class.java)

            paseador?.let {
              val promedioActual = it.promedioPuntuaciones
              if(promedioActual>0){
              val nuevaCalificacion = (promedioActual + calificacion) / 2 // Calcula el nuevo promedio
              userSnapshot.ref.child("promedioPuntuaciones").setValue(nuevaCalificacion)
              }
              else{
                // Calcula el nuevo promedio
                userSnapshot.ref.child("promedioPuntuaciones").setValue(calificacion)
              }
            }
          }
        }
      }

      override fun onCancelled(error: DatabaseError) {
        // Manejar errores si es necesario
      }
    })
  }

    fun updateEstaPaseando(userDni: String, b: Boolean) {
      val usersQuery = paseadoresReference.orderByChild("dni").equalTo(userDni)
      usersQuery.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
          if (snapshot.exists()) {
            for (userSnapshot in snapshot.children) {
              userSnapshot.ref.child("estaPaseando").setValue(b)
            }
          }
        }

        override fun onCancelled(error: DatabaseError) {

        }
      })
    }


}

