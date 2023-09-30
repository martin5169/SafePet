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

        btnPerfilPaseador = v.findViewById(R.id.btnPerfilPaseador)
        btnPasearAhora = v.findViewById(R.id.mapaPaseador2)
        btnHistorial = v.findViewById(R.id.btnHistorialPaseador)
        text = v.findViewById(R.id.welcomeTextPaseador)

        btnMediosCobro = v.findViewById(R.id.mediosDeCobroPaseador2)


        return v
    }
    override fun onStart() {
        super.onStart()

        val user = UserSession.user
        text.text= "Bienvenido, ${user.name}"

        btnMediosCobro.setOnClickListener {
            val action = HomePaseadorDirections.actionHomePaseadorToMediosDeCobroPaseador()
            findNavController().navigate(action)
        }

        btnPerfilPaseador.setOnClickListener {
            val action = HomePaseadorDirections.actionHomePaseadorToPerfilPaseador2()
            findNavController().navigate(action)
        }

        btnHistorial.setOnClickListener {
           val action = HomePaseadorDirections.actionHomePaseadorToPaseadorHistorial()
            findNavController().navigate(action)
        }



    }

}