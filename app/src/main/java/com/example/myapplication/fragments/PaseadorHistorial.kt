package com.example.myapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapters.PaseosProgamadosAdapter
import com.example.myapplication.entities.UserSession
import com.example.myapplication.repository.PaseoProgramadoRepository

class PaseadorHistorial : Fragment() {

    lateinit var v : View
    lateinit var recyclerPaseosPaseador: RecyclerView
    lateinit var paseosRepository: PaseoProgramadoRepository
    lateinit var adapter: PaseosProgamadosAdapter
    lateinit var textoSinPaseos : TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_paseador_historial, container, false)

        recyclerPaseosPaseador = v.findViewById(R.id.recyclerList)
        textoSinPaseos = v.findViewById(R.id.notificacionVacio)
        paseosRepository = PaseoProgramadoRepository.getInstance()

        return v

    }

    override fun onStart() {
        super.onStart()


        val userDNI = UserSession.user.dni
        paseosRepository.getPaseos { paseosList ->
            val paseosFiltrados = paseosList.filter { paseo ->
                paseo.paseador.dni == userDNI
            }
            if (paseosFiltrados.isEmpty()) {
                textoSinPaseos.text = "Aun no tenes paseos programados"
            } else {
                textoSinPaseos.text = "Estos son tus paseos programados"
            }

            adapter = PaseosProgamadosAdapter(paseosFiltrados.toMutableList()) { position ->

                val action =
                    PaseadorHistorialDirections.actionPaseadorHistorialToPaseoProgramadoDetail2(
                        paseosFiltrados[position]
                    )
                findNavController().navigate(action)
            }

            recyclerPaseosPaseador.layoutManager = LinearLayoutManager(context)
            recyclerPaseosPaseador.adapter = adapter
        }


    }

}