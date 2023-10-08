package com.example.myapplication.fragments

import android.annotation.SuppressLint
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

class UserHistorial : Fragment() {

    lateinit var v: View
    lateinit var recyclerPaseosProgramados: RecyclerView
    lateinit var paseosRepository: PaseoProgramadoRepository
    lateinit var adapter : PaseosProgamadosAdapter
    lateinit var textoSinPaseos: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_user_historial, container, false)
       recyclerPaseosProgramados = v.findViewById(R.id.recyclerPaseo)
        paseosRepository = PaseoProgramadoRepository.getInstance()
        textoSinPaseos = v.findViewById(R.id.notificacionVacioUser)

        return v

    }
    @SuppressLint("SuspiciousIndentation")
    override fun onStart() {
        super.onStart()
        val userDNI = UserSession.user.dni
        paseosRepository.getPaseos { paseosList ->
            val passeFiltrates = paseosList.filter { paseo ->
                paseo.user.dni == userDNI
            }

            adapter = PaseosProgamadosAdapter(passeFiltrates.toMutableList()) { position ->
                val action = UserHistorialDirections.actionUserHistorialToPaseoProgramadoDetail(
                    passeFiltrates[position])
                findNavController().navigate(action)
            }

            if (passeFiltrates.isEmpty()) {
                textoSinPaseos.text = "Aun no tenes paseos programados"
            } else {
                textoSinPaseos.text = "Estos son tus paseos programados"
            }

            recyclerPaseosProgramados.layoutManager = LinearLayoutManager(context)
            recyclerPaseosProgramados.adapter = adapter
        }
    }


}








