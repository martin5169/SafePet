package com.example.myapplication.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.google.android.material.snackbar.Snackbar
import com.example.myapplication.entities.Paseador
import com.example.myapplication.repository.PaseadorRepository
import com.example.myapplication.entities.User
import com.example.myapplication.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterForm : Fragment() {

    lateinit var v: View
    lateinit var btnRegister: Button
    lateinit var name: EditText
    lateinit var lastName: EditText
    lateinit var dni: EditText
    lateinit var mail: EditText
    lateinit var pass: EditText
    lateinit var paseadoresRepository: PaseadorRepository
    lateinit var usersRepository: UserRepository
    lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_register_form, container, false)

        btnRegister = v.findViewById(R.id.btnRegister)
        name = v.findViewById(R.id.name)
        lastName = v.findViewById(R.id.lastName)
        dni = v.findViewById(R.id.dni)
        mail = v.findViewById(R.id.dniUser)
        pass = v.findViewById(R.id.pass)
        auth = Firebase.auth

        paseadoresRepository = PaseadorRepository.getInstance()
        usersRepository = UserRepository.getInstance()

        return v
    }

    override fun onStart() {
        super.onStart()
        val seleccion = arguments?.getString("opcion")
        btnRegister.setOnClickListener {
            val enteredName = name.text.toString()
            val enteredLastName = lastName.text.toString()
            val enteredDni = dni.text.toString()
            val enteredMail = mail.text.toString()
            val enteredPass = pass.text.toString()

            if (seleccion == "paseador") {
                paseadoresRepository.getPaseadores { paseadoresList ->
                    val paseadorByDni = paseadoresList.find { it.dni == enteredDni }
                    val paseadorByMail = paseadoresList.find { it.mail == enteredMail }
                    if (paseadorByDni != null) {
                        Snackbar.make(
                            v,
                            "El DNI ingresado ya está registrado.",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    } else if (paseadorByMail != null) {
                        Snackbar.make(
                            v,
                            "El correo electrónico ingresado ya está registrado.",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    } else {
                        val newPaseador = Paseador(
                            enteredName,
                            enteredLastName,
                            enteredPass,
                            enteredDni,
                            enteredMail
                        )
                        paseadoresRepository.addUser(newPaseador)
                        auth.createUserWithEmailAndPassword(enteredMail, enteredPass)
                            .addOnCompleteListener(requireActivity()) { task ->
                                if (task.isSuccessful) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("REGISTER", "createUserWithEmail:success")
                                    val user = auth.currentUser
                                    Snackbar.make(
                                        v,
                                        "Paseador registrado con éxito",
                                        Snackbar.LENGTH_SHORT
                                    )
                                        .show()
                                    val action =
                                        RegisterFormDirections.actionRegisterForm2ToLogin22()
                                    findNavController().navigate(action)
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("REGISTER", "createUserWithEmail:failure", task.exception)
                                    Toast.makeText(
                                        requireContext(),
                                        "Authentication failed.",
                                        Toast.LENGTH_SHORT,
                                    ).show()
                                }
                            }
                    }
                }
            } else {
                usersRepository.getUsers { userList ->
                    val userByDni = userList.find { it.dni == enteredDni }
                    val userByMail = userList.find { it.mail == enteredMail }
                    if (userByDni != null) {
                        Snackbar.make(
                            v,
                            "El DNI ingresado ya está registrado.",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    } else if (userByMail != null) {
                        Snackbar.make(
                            v,
                            "El correo electrónico ingresado ya está registrado.",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    } else {

                        auth.createUserWithEmailAndPassword(enteredMail, enteredPass)
                            .addOnCompleteListener(requireActivity()) { task ->
                                if (task.isSuccessful) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("REGISTER", "createUserWithEmail:success")
                                    val newUser =
                                        User(
                                            enteredName,
                                            enteredLastName,
                                            enteredPass,
                                            enteredDni,
                                            enteredMail
                                        )
                                    usersRepository.addUser(newUser)
                                    Snackbar.make(
                                        v,
                                        "Usuario registrado con éxito",
                                        Snackbar.LENGTH_SHORT
                                    )
                                        .show()
                                    val action =
                                        RegisterFormDirections.actionRegisterForm2ToLogin22()
                                    findNavController().navigate(action)
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("REGISTER", "createUserWithEmail:failure", task.exception)
                                    Toast.makeText(
                                        requireContext(),
                                        "Error al crear el usuario.",
                                        Toast.LENGTH_SHORT,
                                    ).show()
                                }
                            }

                    }
                }
            }
        }
    }

}


