package com.example.myapplication.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.entities.UserSession

class HomePaseador : Fragment() {

    lateinit var v: View
    lateinit var text: TextView
    lateinit var btnHistorial: Button
    lateinit var btnPerfilPaseador : Button
    lateinit var btnMediosCobro: Button
    lateinit var btnPasearAhora: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        v = inflater.inflate(R.layout.fragment_home_paseador, container, false)

        btnPasearAhora = v.findViewById(R.id.mapaPaseador2)

        text = v.findViewById(R.id.welcomeTextPaseador)



        return v
    }
    override fun onStart() {
        super.onStart()

        val user = UserSession.user
        text.text= "Bienvenido, ${user.name}"




    }

}