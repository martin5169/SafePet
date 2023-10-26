package com.example.myapplication.fragments

import com.example.myapplication.adapters.PaseadorAdapter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapters.PaseosProgramadosAdapter
import com.example.myapplication.entities.EstadoEnum
import com.example.myapplication.entities.Paseador
import com.example.myapplication.entities.PaseoProgramado
import com.example.myapplication.entities.UserSession
import com.example.myapplication.repository.PaseadorRepository

class PaseadoresList : Fragment() {

    lateinit var v : View
    lateinit var recyclerPaseadores: RecyclerView
    lateinit var paseadoresRepository: PaseadorRepository
    lateinit var txtTitle : TextView
    lateinit var spinner : Spinner
    lateinit var adaptador: ArrayAdapter<String>
    val estados = mutableListOf("TODOS","ACTIVO")
    var paseadoresOriginales: List<Paseador> = mutableListOf()
    var paseadoresFiltrados: List<Paseador> = mutableListOf()
    lateinit var adapter: PaseadorAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_paseadores_list, container, false)
        recyclerPaseadores = v.findViewById(R.id.recyclerUser)
        paseadoresRepository = PaseadorRepository.getInstance()
        spinner = v.findViewById(R.id.filtroEstadosPaseadores)
        adaptador = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, estados)
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adaptador

        adapter = PaseadorAdapter(mutableListOf()) { position ->
            val action = PaseadoresListDirections.actionPaseadoresListToPaseadorDetail(paseadoresOriginales[position])
            findNavController().navigate(action)
        }

        recyclerPaseadores.layoutManager = LinearLayoutManager(context)
        recyclerPaseadores.adapter = adapter


        return v
    }


    override fun onStart() {
        super.onStart()
        spinner.setSelection(0)


            paseadoresRepository.getPaseadores { paseadoresList ->
                paseadoresOriginales = paseadoresList.sortedBy { -it.promedioPuntuaciones }
                adapter = PaseadorAdapter(paseadoresOriginales.toMutableList()) { position ->
                    val action = PaseadoresListDirections.actionPaseadoresListToPaseadorDetail(paseadoresOriginales[position])
                    findNavController().navigate(action)
                }

                adapter.paseadores = paseadoresOriginales.toMutableList()
                recyclerPaseadores.layoutManager = LinearLayoutManager(context)
                recyclerPaseadores.adapter = adapter
            }

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                    val selectedValue = spinner.selectedItem.toString()
                    if (selectedValue == "TODOS") {
                        adapter.paseadores = paseadoresOriginales.toMutableList()
                    } else if (selectedValue == "ACTIVO") {
                        adapter.paseadores = paseadoresOriginales.filter { it.estaPaseando }.toMutableList()
                    }
                    adapter.notifyDataSetChanged()
                    Log.d("CHANGE", adapter.paseadores.joinToString())
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) {
                    // Code to execute when nothing is selected
                }
            }
        }


}