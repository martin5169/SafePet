package com.example.myapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.myapplication.R
import com.example.myapplication.entities.UserAbstract
import com.example.myapplication.entities.UserSession
import com.example.myapplication.repository.PaseadorRepository
import com.google.android.material.snackbar.Snackbar

class PerfilPaseador : Fragment() {

    lateinit var v: View
    lateinit var name: TextView
    lateinit var lastName: TextView
    lateinit var dni: TextView
    lateinit var contraseña : EditText
    lateinit var mail : EditText
    lateinit var btnEdit: Button

    private lateinit var viewModel: PerfilPaseadorViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v =  inflater.inflate(R.layout.fragment_perfil_paseador, container, false)


        name = v.findViewById(R.id.namePaseador)
        lastName = v.findViewById(R.id.lastNamePaseador)
        dni = v.findViewById(R.id.dniPaseador)
        contraseña = v.findViewById(R.id.paseadorContraseña)
        mail = v.findViewById(R.id.paseadorMail2)
        btnEdit = v.findViewById(R.id.editPerfil2)

        return v
    }

    override fun onStart() {
        super.onStart()
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
                showConfirmationDialog(user, enteredMail, enteredPassword)
            } else {
                Snackbar.make(v, "Todos los campos son requeridos", Snackbar.LENGTH_SHORT).show()
            }
        }

    }
    private fun showConfirmationDialog(user: UserAbstract, enteredMail: String, enteredPassword: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Confirmación")
        builder.setMessage("¿Estás seguro de actualizar tus datos de perfil?")

        builder.setPositiveButton("Sí") { _, _ ->
            // Usuario confirmó, realizar la actualización de datos aquí
            val paseadorRepository = PaseadorRepository.getInstance()
            paseadorRepository.updatePaseador(user.dni, enteredMail, enteredPassword)
            Snackbar.make(v, "Datos actualizados con éxito", Snackbar.LENGTH_SHORT).show()
            user.mail = enteredMail
            user.password = enteredPassword
        }

        builder.setNegativeButton("No") { _, _ ->
            // Usuario canceló, no se realiza la actualización
        }

        val dialog = builder.create()
        dialog.show()
    }

}