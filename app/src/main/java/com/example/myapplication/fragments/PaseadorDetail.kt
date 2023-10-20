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
import com.google.android.material.snackbar.Snackbar

class PaseadorDetail : Fragment(){

    lateinit var v : View
    lateinit var paseadorLastname : TextView
    lateinit var paseadorName : TextView
    lateinit var paseadorDni : TextView
    lateinit var paseadorMail : TextView
    lateinit var paseadorTarifa : TextView
    lateinit var promedioCal : TextView
    lateinit var btnSolicitarPaseo : Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_paseador_detail, container, false)
        paseadorLastname = v.findViewById(R.id.paseadorLastName)
        paseadorName = v.findViewById(R.id.paseadorName)
        paseadorDni = v.findViewById(R.id.paseadorDni)
        promedioCal = v.findViewById(R.id.promedioCal)
        paseadorMail = v.findViewById(R.id.paseadorMail)
        paseadorTarifa = v.findViewById(R.id.paseadorTarifa)
        btnSolicitarPaseo = v.findViewById(R.id.btnSolicitarPaseo)

        return v
    }
    override fun onStart() {
        super.onStart()
        val paseador = PaseadorDetailArgs.fromBundle(requireArguments()).paseador
        paseadorLastname.text= paseador.lastName
        paseadorName.text= paseador.name
        paseadorDni.text= paseador.dni
        paseadorMail.text= paseador.mail
        paseadorTarifa.text = if (paseador.tarifa.toString() != null) {
            paseador.tarifa.toString()
        } else {
            "$0"
        }

        promedioCal.text =  if (paseador.promedioPuntuaciones == 0) {
            "Aun no se puede calcular"
        } else {
            paseador.promedioPuntuaciones.toString()
        }



        btnSolicitarPaseo.setOnClickListener{
            val action = PaseadorDetailDirections.actionPaseadorDetailToCalendarioPaseador(paseador)
            findNavController().navigate(action)
        }


    }



}