package com.example.myapplication.fragments

import android.app.AlertDialog
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
import com.example.myapplication.entities.Paseador
//<<<<<<< HEAD
import com.example.myapplication.entities.PaseoProgramado
import com.example.myapplication.entities.User
import com.example.myapplication.entities.UserAbstract
import com.example.myapplication.entities.UserSession
import com.example.myapplication.repository.PaseoProgramadoRepository
import com.google.android.material.snackbar.Snackbar

class PaseoProgramadoDetail : Fragment() {


         lateinit var v: View
         lateinit var fechaPaseo: TextView
         lateinit var duenioPaseo: TextView
         lateinit var mascota: TextView
         lateinit var btnIniciarPaseo: Button
         lateinit var btnCancelarPaseo: Button
        lateinit var paseosRepository: PaseoProgramadoRepository

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {

            v = inflater.inflate(R.layout.fragment_paseo_programado_detail, container, false)

            fechaPaseo = v.findViewById(R.id.fechaPaseo)
            duenioPaseo = v.findViewById(R.id.dueño)
            mascota = v.findViewById(R.id.mascota)
            btnIniciarPaseo = v.findViewById(R.id.btnIniciarPaseo)
            btnCancelarPaseo = v.findViewById(R.id.btnCancelarPaseo)
            paseosRepository = PaseoProgramadoRepository.getInstance()

            return v

        }

        override fun onStart() {
            super.onStart()
                 val paseo =
                PaseoProgramadoDetailArgs.fromBundle(requireArguments()).paseoProgramadodetalle
            fechaPaseo.text = paseo.fecha
            duenioPaseo.text = "${paseo.user.lastName}, ${paseo.user.name} "
            mascota.text = paseo.user.mascota.nombre


            btnIniciarPaseo.setOnClickListener {

            }

            btnCancelarPaseo.setOnClickListener {

                showConfirmationDialog(paseo)

            }


        }

    private fun showConfirmationDialog(paseo: PaseoProgramado) {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle("Confirmación")
        builder.setMessage("¿Estás seguro de eliminar este paseo?")
        builder.setPositiveButton("Sí") { _, _ ->
            // Usuario confirma eliminación
            paseosRepository.deletePaseo(paseo)
            Snackbar.make(v, "Paseo eliminado con éxito", Snackbar.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }

        builder.setNegativeButton("No") { _, _ ->
            // Usuario canceló, no se realiza la actualización
        }

        val dialog = builder.create()
        dialog.show()
    }


    //fun comenzarPaseo() {
        //    viewModel.createLocationCallback(userSession.dni)
        //    viewModel.startLocationUpdates()
        // }

    }
