package com.example.myapplication.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapters.PaseosProgramadosAdapter
import com.example.myapplication.entities.UserSession
import com.example.myapplication.repository.PaseoProgramadoRepository

class PaseadorHistorial : Fragment() {

    lateinit var v : View
    lateinit var recyclerPaseosPaseador: RecyclerView
    lateinit var paseosRepository: PaseoProgramadoRepository
    lateinit var adapter: PaseosProgramadosAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_paseador_historial, container, false)

        recyclerPaseosPaseador = v.findViewById(R.id.recyclerList)
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
            adapter = PaseosProgramadosAdapter(paseosFiltrados.toMutableList()) { position ->

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