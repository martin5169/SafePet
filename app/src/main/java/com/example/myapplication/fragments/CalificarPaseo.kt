package com.example.myapplication.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.entities.UserAbstract
import com.example.myapplication.entities.UserSession
import com.example.myapplication.repository.PaseoRepository
import com.google.android.material.snackbar.Snackbar

class CalificarPaseo : Fragment() {

    private lateinit var v: View
    private lateinit var nombrePaseador: TextView
    lateinit var spinner : Spinner
    lateinit var adaptador: ArrayAdapter<Int>
    lateinit var btnConfirm : Button
    val calificaciones = mutableListOf(1, 2, 3,4,5)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v =  inflater.inflate(R.layout.fragment_calificar_paseo, container, false)
        nombrePaseador = v.findViewById(R.id.nombrePaseador)
        btnConfirm = v.findViewById(R.id.btnConfirmCalificacion)
        spinner = v.findViewById(R.id.filtro)
        adaptador = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, calificaciones)
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adaptador


        return v
    }

    override fun onStart() {
        super.onStart()
        // UserSession.user = UserSession.user as User
        val paseo =
            CalificarPaseoArgs.fromBundle(requireArguments()).paseoProgramadoACalificar
        nombrePaseador.text = "${paseo.paseador.lastName},${paseo.paseador.name}"

        btnConfirm.setOnClickListener {
            val selectedCalif = spinner.selectedItem
            paseo.calificacion = selectedCalif as Int
            showConfirmationDialog(UserSession.user, selectedCalif)
        }

    }

    private fun showConfirmationDialog(user: UserAbstract, calif: Int) {
        val builder = AlertDialog.Builder(requireContext())
        val paseo =
            CalificarPaseoArgs.fromBundle(requireArguments()).paseoProgramadoACalificar
        val paseoRepo = PaseoRepository.getInstance()
        builder.setTitle("Confirmación")
        builder.setMessage("¿Confirma la calificacion seleccionada?")
        builder.setPositiveButton("Sí") { _, _ ->
            // USUARIO CONFIRMA, ACTUALIZA LA CALIFICACION EN LA BD Y EN EL PASEO
            paseoRepo.updateCalificacion(paseo.id,calif)
            paseo.calificacion = calif
            Snackbar.make(v, "Calificacion asignada con éxito", Snackbar.LENGTH_SHORT).show()
            val action = CalificarPaseoDirections.actionCalificarPaseoToUserHistorial()
            findNavController().navigate(action)
        }

        builder.setNegativeButton("No") { _, _ ->
            // Usuario canceló, no se realiza la actualización
        }

        val dialog = builder.create()
        dialog.show()
    }



}