package com.example.myapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.myapplication.R
import com.example.myapplication.entities.UserSession
import com.google.android.material.snackbar.Snackbar

class PerfilUser : Fragment() {

    lateinit var v: View
    lateinit var name: TextView
    lateinit var lastName: TextView
    lateinit var mail: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(
            R.layout.fragment_perfil_user, container, false
        )

        name = v.findViewById(R.id.name)
        lastName = v.findViewById(R.id.lastName)
        mail = v.findViewById(R.id.mail)

        return v
    }

    override fun onStart() {
        super.onStart()
        try {
            val user = UserSession.user
            if (user != null) {
                name.text = "Nombre: ${user.name}"
                lastName.text = "Apellido: ${user.lastName}"
                mail.text = "Email: ${user.mail}"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Snackbar.make(v, "No se encuentran los datos", Snackbar.LENGTH_SHORT).show()
        }

    }

}