package com.example.myapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.R
import com.example.myapplication.entities.UserSession
import com.example.myapplication.repository.UserRepository
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.example.myapplication.entities.User
import com.example.myapplication.entities.UserAbstract
import com.example.myapplication.repository.PaseadorRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class PerfilUser : Fragment() {

    lateinit var v: View
    lateinit var name: TextView
    lateinit var lastName: TextView
    lateinit var dni: TextView
    lateinit var contraseña : Button
    lateinit var mail : TextView
    lateinit var btnEdit: Button
    lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(
            R.layout.fragment_perfil_user, container, false
        )

        name = v.findViewById(R.id.name)
        lastName = v.findViewById(R.id.lastName)
        dni = v.findViewById(R.id.dniUser)
        contraseña = v.findViewById(R.id.cambiarPass2)
        mail = v.findViewById(R.id.userMail)
        btnEdit = v.findViewById(R.id.editPerfil)
        auth = Firebase.auth

        return v
    }

    override fun onStart() {
        super.onStart()
        val user = UserSession.user
        if (user != null) {
            name.text = user.name
            lastName.text = user.lastName
            dni.text = user.dni
            mail.text = user.mail
        }
        btnEdit.setOnClickListener {
            val enteredMail = mail.text.toString()

            if (enteredMail.isNotEmpty()) {
                showConfirmationDialog(user, enteredMail)

            } else {
                Snackbar.make(v, "Todos los campos son requeridos", Snackbar.LENGTH_SHORT).show()
            }
        }
        contraseña.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Cambio de Contraseña")
            builder.setMessage("Enviaremos un mail para que actualices tu contraseña, ¿deseas continuar?")
            builder.setPositiveButton("Sí") { _, _ ->
                auth.sendPasswordResetEmail(user.mail)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                requireContext(),
                                "Se envió el correo de cambio de contraseña.",
                                Toast.LENGTH_SHORT,
                            ).show()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Fallo el envio del correo.",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    }
            }
            builder.setNegativeButton("No") { _, _ ->
                // Usuario cancela, no se realiza la actualizacion
            }
            val dialog = builder.create()
            dialog.show()
        }

    }
    private fun showConfirmationDialog(user: UserAbstract, enteredMail: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Confirmación")
        builder.setMessage("¿Estás seguro de actualizar tus datos de perfil?")

        builder.setPositiveButton("Sí") { _, _ ->
            // Usuario confirma, realiza la actualización
            val paseadorRepository = PaseadorRepository.getInstance()
            paseadorRepository.updatePaseador(user.dni, enteredMail, "")
            Snackbar.make(v, "Datos actualizados con éxito", Snackbar.LENGTH_SHORT).show()
            user.mail = enteredMail
            user.password = ""
            val action = PerfilPaseadorDirections.actionPerfilPaseador2ToHomePaseador()
            findNavController().navigate(action)
        }

        builder.setNegativeButton("No") { _, _ ->
            // Usuario cancela, no se realiza la actualizacion
        }

        val dialog = builder.create()
        dialog.show()
    }

}