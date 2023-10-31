package com.example.myapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapters.PaseosProgramadosAdapter
import com.example.myapplication.entities.UserSession
import com.example.myapplication.repository.PaseoRepository
import java.text.SimpleDateFormat

class UserHistorial : Fragment() {

    lateinit var v: View
    lateinit var recyclerPaseosList: RecyclerView
    lateinit var paseosRepository: PaseoRepository
    lateinit var adapter: PaseosProgramadosAdapter
    lateinit var textoSinPaseos: TextView
    lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_user_historial, container, false)

        recyclerPaseosList = v.findViewById(R.id.recyclerPaseo)
        paseosRepository = PaseoRepository.getInstance()
        textoSinPaseos = v.findViewById(R.id.notificacionVacioUser)

        progressBar = v.findViewById(R.id.progressBar)

        return v

    }

    override fun onStart() {
        super.onStart()
        val userDNI = UserSession.user.dni
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm")

        paseosRepository.getPaseos { paseosList ->
            var paseosFiltrados = paseosList.filter { paseo ->
                paseo.user.dni == userDNI
            }.toMutableList()
            paseosFiltrados = paseosFiltrados.sortedWith(compareByDescending {  format.parse(it.fecha)?.time }).toMutableList()
            adapter = PaseosProgramadosAdapter(paseosFiltrados) { position ->
                val action = UserHistorialDirections.actionUserHistorialToPaseoProgramadoDetail(
                    paseosFiltrados[position]
                )
                findNavController().navigate(action)
            }

            recyclerPaseosList.layoutManager = LinearLayoutManager(context)
            recyclerPaseosList.adapter = adapter

            if (paseosFiltrados.isEmpty()) {
                textoSinPaseos.text = "Aún no tienes paseos programados"
            } else {
                textoSinPaseos.text = "Estos son tus paseos programados"
            }

            recyclerPaseosList.layoutManager = LinearLayoutManager(context)
            recyclerPaseosList.adapter = adapter

            progressBar.visibility = View.GONE
        }

    }
}


