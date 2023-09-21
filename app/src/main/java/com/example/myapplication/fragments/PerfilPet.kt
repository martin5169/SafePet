package com.example.myapplication.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.navigation.fragment.findNavController
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
        val pet = (user as User).mascota

        val petNameNotEmpty = pet.nombre.isNotEmpty()
        val petWeightNotEmpty = pet.peso.isNotEmpty()
        val petAgeNotEmpty = pet.edad.isNotEmpty()
        val petBreedNotEmpty = pet.raza.isNotEmpty()

        if (petNameNotEmpty && petWeightNotEmpty && petAgeNotEmpty && petBreedNotEmpty) {
            btnRegister.text = "Editar datos"

            inputPetName.setText(pet.nombre)
            inputPetWeight.setText(pet.peso)
            inputPetAge.setText(pet.edad)
            inputPetBreed.setText(pet.raza)

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
                showConfirmationDialog(user, enteredName, enteredWeight, enteredAge, enteredBreed)
                            } else {
                Snackbar.make(v, "Todos los campos son requeridos", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun showConfirmationDialog(user: UserAbstract, enteredName: String, enteredWeight: String, enteredAge: String, enteredBreed: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Confirmación")
        builder.setMessage("¿Estás seguro de actualizar los datos de la mascota?")

        builder.setPositiveButton("Sí") { _, _ ->
            // Usuario confirma, realiza la actualización de datos
            val userRepository = UserRepository.getInstance()
            userRepository.updatePet(user.dni, enteredName, enteredWeight, enteredAge, enteredBreed)
            btnRegister.visibility = View.GONE
            (user as User).mascota.nombre = enteredName
            user.mascota.peso = enteredWeight
            user.mascota.edad = enteredAge
            user.mascota.raza = enteredBreed

            Snackbar.make(v, "Datos de la mascota actualizados con éxito", Snackbar.LENGTH_SHORT).show()
            val action = PerfilPetDirections.actionPerfilPetToHome()
            findNavController().navigate(action)
        }

        builder.setNegativeButton("No") { _, _ ->
            // Usuario canceló, no se realiza la actualización
        }

        val dialog = builder.create()
        dialog.show()
    }
}

