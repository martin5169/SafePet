package com.example.myapplication.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.entities.EstadoEnum
import com.example.myapplication.entities.PaseoProgramado
import com.example.myapplication.entities.User
import com.example.myapplication.entities.UserSession
import com.example.myapplication.repository.PaseoRepository
import java.text.SimpleDateFormat
import java.util.Date

class PaseadorDetail : Fragment(){

    lateinit var v : View
    lateinit var paseadorLastname : TextView
    lateinit var paseadorName : TextView
    lateinit var paseadorDni : TextView
    lateinit var paseadorMail : TextView
    lateinit var paseadorTarifa : TextView
    lateinit var promedioCal : TextView
    lateinit var btnSolicitarPaseo : Button
    lateinit var btnSolicitarPaseoInstantaneo : Button
    val paseoProgramadoRepository = PaseoRepository.getInstance()


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
        btnSolicitarPaseoInstantaneo = v.findViewById(R.id.paseoInstantaneo)

        return v
    }
    override fun onStart() {
        super.onStart()
        val paseador = PaseadorDetailArgs.fromBundle(requireArguments()).paseador
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm")
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
        var fechaActual = Date()
        fechaActual = Date(fechaActual.time - 10800000)
        Log.d("ESTA PASEANDO", paseador.estaPaseando.toString())
        if(!paseador.estaPaseando) {
            Log.d("ESTA PASEANDO", "ESTA PASEANDO")
            btnSolicitarPaseoInstantaneo.visibility = View.GONE
        }else {
            btnSolicitarPaseoInstantaneo.visibility = View.VISIBLE
        }

        btnSolicitarPaseoInstantaneo.setOnClickListener {

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Confirmación")
            builder.setMessage("¿Esta seguro de solicitar el paseo? Se enviará una solicitud de manera instantanea al paseador")
            builder.setPositiveButton("Sí") { _, _ ->
                val paseoProgramado = PaseoProgramado(paseador, UserSession.user as User, "${format.format(fechaActual)}hs", EstadoEnum.SOLICITADO,0)
                paseoProgramadoRepository.addPaseo(paseoProgramado)
                findNavController().popBackStack()
            }

            builder.setNegativeButton("No") { _, _ ->
                // Usuario canceló, no se realiza la actualización
            }

            val dialog = builder.create()
            dialog.show()
        }

        btnSolicitarPaseo.setOnClickListener{
            val action = PaseadorDetailDirections.actionPaseadorDetailToCalendarioPaseador(paseador)
            findNavController().navigate(action)
        }

    }



}