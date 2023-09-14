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
        val userRepository = UserRepository.getInstance()
        val user = UserSession.user
        if (user != null) {
            name.text = "Nombre: ${user.name}"
            lastName.text = "Apellido: ${user.lastName}"
            dni.text = "DNI: ${user.dni}"
            contraseña.setText(user.password)
            mail.setText(user.mail)
        }

        btnEdit.setOnClickListener {
            val enteredMail = mail.text.toString()
            val enteredPassword = contraseña.text.toString()

            if (enteredMail.isNotEmpty() && enteredPassword.isNotEmpty()) {
                userRepository.updateUser(user.dni, enteredMail, enteredPassword)
                Snackbar.make(v, "Datos registrados con éxito", Snackbar.LENGTH_SHORT).show()
                user.mail=enteredMail
                user.password=enteredPassword
            } else {
                Snackbar.make(v, "Todos los campos son requeridos", Snackbar.LENGTH_SHORT).show()
            }


        }
    }
}