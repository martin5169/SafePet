package com.example.myapplication.repository

import com.example.myapplication.entities.Pet
import com.google.firebase.database.*

class PetRepository() {
  private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
  private val petsReference: DatabaseReference = database.getReference("pets")

  companion object {
    @Volatile
    private var instance: PetRepository? = null

    fun getInstance(): PetRepository {
      return instance ?: synchronized(this) {
        instance ?: PetRepository().also { instance = it }
      }
    }
  }

  // Agregar un paseador a la base de datos
  fun addPet(pet: Pet) {
    val petKey = petsReference.push().key // Generar una clave única para la mascota
    petKey?.let {
      petsReference.child(it).setValue(pet)
    }
  }

  // Recuperar la lista de mascotas desde la base de datos
  fun getPets(callback: (List<Pet>) -> Unit) {
    petsReference.addListenerForSingleValueEvent(object : ValueEventListener {
      override fun onDataChange(snapshot: DataSnapshot) {
        val petList = mutableListOf<Pet>()
        for (childSnapshot in snapshot.children) {
          val pet = childSnapshot.getValue(Pet::class.java)
          pet?.let {
            petList.add(it)
          }
        }
        callback(petList)
      }

      override fun onCancelled(error: DatabaseError) {

      }
    })
  }
}

