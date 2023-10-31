package com.example.myapplication.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapters.PaseosProgramadosAdapter
import com.example.myapplication.entities.EstadoEnum
import com.example.myapplication.entities.PaseoProgramado
import com.example.myapplication.entities.UserSession
import com.example.myapplication.repository.PaseoRepository
import java.text.SimpleDateFormat

class UserHistorial : Fragment() {

    lateinit var v : View
    lateinit var recyclerPaseosList: RecyclerView
    lateinit var paseosRepository: PaseoRepository
    lateinit var adapter: PaseosProgramadosAdapter
    lateinit var textoSinPaseos : TextView
    lateinit var spinner : Spinner
    lateinit var adaptador: ArrayAdapter<String>
    lateinit var paseos: List<PaseoProgramado>
    lateinit var progressBar: ProgressBar
    val estados = mutableListOf("TODOS","ACTIVO", "NO_ACTIVO", "FINALIZADO")
    var paseosOriginales: List<PaseoProgramado> = mutableListOf()
    var paseosFiltrados: List<PaseoProgramado> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_user_historial, container, false)

        recyclerPaseosList = v.findViewById(R.id.recyclerPaseo)
        paseosRepository = PaseoRepository.getInstance()
        textoSinPaseos = v.findViewById(R.id.notificacionVacioUser)
        spinner = v.findViewById(R.id.filtroEstadosDuenio)
        adaptador = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, estados)
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adaptador


        progressBar = v.findViewById(R.id.progressBar)

        return v

    }

    override fun onStart() {
        super.onStart()
        spinner.setSelection(0)
        val userDNI = UserSession.user.dni
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm")
        paseosRepository.getPaseos { paseosList ->
            paseosFiltrados = paseosList.filter { paseo ->
                paseo.user.dni == userDNI
            }
            if (paseosFiltrados.isEmpty()) {
                textoSinPaseos.text = "Aun no tenes paseos"
            } else {
                textoSinPaseos.text = "Estos son tus paseos"
            }

            paseosOriginales = paseosFiltrados
            paseosFiltrados = paseosFiltrados.sortedWith(compareByDescending {  format.parse(it.fecha)?.time })

            adapter.paseos = paseosFiltrados.toMutableList()
            paseosFiltrados = adapter.paseos
            adapter.notifyDataSetChanged()
            progressBar.visibility = View.GONE
        }

        adapter = PaseosProgramadosAdapter(paseosFiltrados.toMutableList()) { position ->

            val action =
                UserHistorialDirections.actionUserHistorialToPaseoProgramadoDetail(
                    paseosFiltrados[position]
                )
            findNavController().navigate(action)
        }

        recyclerPaseosList.layoutManager = LinearLayoutManager(context)
        recyclerPaseosList.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                val selectedValue = spinner.selectedItem.toString()
                if(selectedValue == "TODOS") {
                    adapter.paseos = paseosOriginales.toMutableList()
                    paseosFiltrados = adapter.paseos
                }else {
                    adapter.paseos =
                        paseosOriginales.filter { it.estado == EstadoEnum.valueOf(selectedValue) }.toMutableList()
                    paseosFiltrados = adapter.paseos
                }

                adapter.notifyDataSetChanged()
                Log.d("CHANGE", paseosFiltrados.joinToString())
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Aquí se ejecutará el código cuando no se haya seleccionado ningún elemento
            }
        }
    }
}


