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
import com.example.myapplication.entities.UserSession
import com.example.myapplication.repository.UserRepository
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.example.myapplication.entities.User


class PerfilUser : Fragment() {

    lateinit var v: View
    lateinit var name: TextView
    lateinit var lastName: TextView
    lateinit var dni: TextView
    lateinit var contraseña : EditText
    lateinit var mail : EditText
    lateinit var btnEdit: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(
            R.layout.fragment_perfil_user, container, false
        )

        name = v.findViewById(R.id.name)
        lastName = v.findViewById(R.id.lastName)
        dni = v.findViewById(R.id.dniUser)
        contraseña = v.findViewById(R.id.userContraseña)
        mail = v.findViewById(R.id.userMail)
        btnEdit = v.findViewById(R.id.editPerfil)

        return v
    }

    override fun onStart() {
        super.onStart()
        val user = UserSession.user
        if (user != null) {
            name.text = user.name
            lastName.text = user.lastName
            dni.text = user.dni
            contraseña.setText(user.password)
            mail.setText(user.mail)
        }

        btnEdit.setOnClickListener {
            val enteredMail = mail.text.toString()
            val enteredPassword = contraseña.text.toString()

            if (enteredMail.isNotEmpty() && enteredPassword.isNotEmpty()) {
                showConfirmationDialog(user as User, enteredMail, enteredPassword)
            } else {
                Snackbar.make(v, "Todos los campos son requeridos", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun showConfirmationDialog(user: User, enteredMail: String, enteredPassword: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Confirmación")
        builder.setMessage("¿Estás seguro de actualizar tus datos de perfil?")

        builder.setPositiveButton("Sí") { _, _ ->
            // Usuario confirmó, realizar la actualización de datos aquí
            val userRepository = UserRepository.getInstance()
            userRepository.updateUser(user.dni, enteredMail, enteredPassword)
            Snackbar.make(v, "Datos actualizados con éxito", Snackbar.LENGTH_SHORT).show()
            user.mail = enteredMail
            user.password = enteredPassword
            val action = PerfilUserDirections.actionPerfilUserToHome()
            findNavController().navigate(action)
        }

        builder.setNegativeButton("No") { _, _ ->
            // Usuario canceló, no se realiza la actualización
        }

        val dialog = builder.create()
        dialog.show()
    }
}