package entities

import com.google.firebase.database.*

class UserRepository() {
  private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
  private val usersReference: DatabaseReference = database.getReference("users")

  companion object {
    @Volatile
    private var instance: UserRepository? = null

    fun getInstance(): UserRepository {
      return instance ?: synchronized(this) {
        instance ?: UserRepository().also { instance = it }
      }
    }
  }

  // Agregar un paseador a la base de datos
  fun addUser(user: User ) {
    val userKey = usersReference.push().key // Generar una clave única para el paseador
    userKey?.let {
      usersReference.child(it).setValue(user)
    }
  }

  // Recuperar la lista de usuarios desde la base de datos
  fun getUsers(callback: (List<User>) -> Unit) {
    usersReference.addListenerForSingleValueEvent(object : ValueEventListener {
      override fun onDataChange(snapshot: DataSnapshot) {
        val userList = mutableListOf<User>()
        for (childSnapshot in snapshot.children) {
          val user = childSnapshot.getValue(User::class.java)
          user?.let {
            userList.add(it)
          }
        }
        callback(userList)
      }

      override fun onCancelled(error: DatabaseError) {

      }
    })
  }

  fun updatePet(userDni: String, petName: String, petWeight: String, petAge: String, petBreed: String) {
    // Buscar al usuario por su DNI en la base de datos
    val usersQuery = usersReference.orderByChild("dni").equalTo(userDni)

    usersQuery.addListenerForSingleValueEvent(object : ValueEventListener {
      override fun onDataChange(snapshot: DataSnapshot) {
        if (snapshot.exists()) {
          for (userSnapshot in snapshot.children) {
            // Actualizar los datos de la mascota del usuario encontrado
            userSnapshot.child("mascota").child("nombre").getRef().setValue(petName)
            userSnapshot.child("mascota").child("peso").getRef().setValue(petWeight)
            userSnapshot.child("mascota").child("edad").getRef().setValue(petAge)
            userSnapshot.child("mascota").child("raza").getRef().setValue(petBreed)
          }
        } else {
          // No se encontró ningún usuario con el DNI proporcionado
          // Puedes manejar esto como mejor te parezca, por ejemplo, lanzando una excepción o mostrando un mensaje de error.
        }
      }

      override fun onCancelled(error: DatabaseError) {
        // Maneja el error aquí si es necesario
      }
    })
  }

}

