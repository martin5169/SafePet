package com.example.myapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.myapplication.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.FirebaseDatabase
import com.example.myapplication.entities.User
import com.example.myapplication.entities.UserAbstract
import com.example.myapplication.entities.UserSession
import com.example.myapplication.repository.UserRepository

class PerfilPet : Fragment() {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()

    lateinit var v: View
    lateinit var btnRegister: Button

    lateinit var inputPetName: EditText
    lateinit var inputPetWeight: EditText
    lateinit var inputPetAge: EditText
    lateinit var inputPetBreed: EditText
    lateinit var alert : TextView

    lateinit var user: UserAbstract

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_perfil_pet, container, false)
        btnRegister = v.findViewById(R.id.btnPerfilPetRegister)

        inputPetName = v.findViewById(R.id.petName)
        inputPetWeight = v.findViewById(R.id.petWeight)
        inputPetAge = v.findViewById(R.id.petAge)
        inputPetBreed = v.findViewById(R.id.petBreed)

        alert = v.findViewById(R.id.Alert)

        return v
    }

    override fun onStart() {
        super.onStart()
        user = UserSession.user as User
        val userRepository = UserRepository.getInstance()

        val petNameNotEmpty = (user as User).getPetName().isNotEmpty()
        val petWeightNotEmpty = (user as User).getPetWeight().isNotEmpty()
        val petAgeNotEmpty = (user as User).getPetAge().isNotEmpty()
        val petBreedNotEmpty = (user as User).getPetBreed().isNotEmpty()


        if (petNameNotEmpty && petWeightNotEmpty && petAgeNotEmpty && petBreedNotEmpty) {
            btnRegister.text = "Editar datos"
            inputPetName.setText((user as User).getPetName())
            inputPetWeight.setText((user as User).getPetWeight())
            inputPetAge.setText((user as User).getPetAge())
            inputPetBreed.setText((user as User).getPetBreed())

        } else {
            btnRegister.visibility = View.VISIBLE
            alert.text = "Aún no has registrado a tu mascota"
        }

        btnRegister.setOnClickListener {
            val enteredName = inputPetName.text.toString()
            val enteredWeight = inputPetWeight.text.toString()
            val enteredAge = inputPetAge.text.toString()
            val enteredBreed = inputPetBreed.text.toString()


            if (enteredName.isNotEmpty() && enteredWeight.isNotEmpty() && enteredAge.isNotEmpty() && enteredBreed.isNotEmpty()) {
                userRepository.updatePet(user.dni, enteredName, enteredWeight, enteredAge, enteredBreed)
                btnRegister.visibility = View.GONE
                Snackbar.make(v, "Datos registrados con éxito", Snackbar.LENGTH_SHORT).show()
            } else {
                Snackbar.make(v, "Todos los campos son requeridos", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}

