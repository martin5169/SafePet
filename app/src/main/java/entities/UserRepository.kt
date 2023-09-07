package entities

import android.util.Log
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

  // Agregar un usuario a la base de datos
  fun addUser(user: User) {
    val userKey = usersReference.push().key // Generar una clave única para el usuario
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
}

