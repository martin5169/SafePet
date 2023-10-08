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
//<<<<<<< HEAD
import com.example.myapplication.entities.PaseoProgramado
import com.example.myapplication.entities.User
import com.example.myapplication.entities.UserAbstract
import com.example.myapplication.entities.UserSession
import com.example.myapplication.repository.PaseoProgramadoRepository
import com.google.android.material.snackbar.Snackbar

class PaseoProgramadoDetail : Fragment() {

//======
//import com.example.myapplication.entities.UserAbstract
//import com.example.myapplication.entities.UserSession
//import com.google.android.gms.location.FusedLocationProviderClient
//import com.google.android.gms.location.LocationServices

    class PaseoProgramadoDetail : Fragment() {

        //lateinit var location: FusedLocationProviderClient
        lateinit var userSession: UserAbstract

        companion object {
            fun newInstance() = PaseoProgramadoDetail()
        }
//>>>>>>> 7891575ee226584d484c911c4725becb1af17fa2

        private lateinit var v: View
        private lateinit var fechaPaseo: TextView
        private lateinit var duenioPaseo: TextView
        private lateinit var mascota: TextView
        private lateinit var btnIniciarPaseo: Button
        private lateinit var btnCancelarPaseo: Button
        lateinit var paseosRepository: PaseoProgramadoRepository


        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
//<<<<<<< HEAD
            v = inflater.inflate(R.layout.fragment_paseo_programado_detail, container, false)

            fechaPaseo = v.findViewById(R.id.fechaPaseo)
            duenioPaseo = v.findViewById(R.id.dueño)
            mascota = v.findViewById(R.id.mascota)
            btnIniciarPaseo = v.findViewById(R.id.btnIniciarPaseo)
            btnCancelarPaseo = v.findViewById(R.id.btnCancelarPaseo)
            paseosRepository = PaseoProgramadoRepository.getInstance()

            return v
//=======
            //       userSession = UserSession.user
            //       location = LocationServices.getFusedLocationProviderClient(requireContext())
            //      return inflater.inflate(R.layout.fragment_paseo_programado_detail, container, false)
//>>>>>>> 7891575ee226584d484c911c4725becb1af17fa2
        }

        override fun onStart() {
            super.onStart()
            UserSession.user = UserSession.user as User
            val paseo =
                PaseoProgramadoDetailArgs.fromBundle(requireArguments()).paseoProgramadoDetalle
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
                // USUARIO CONFIRMA, HACER VALIDACIÓN DE FECHA DISPONIBLE
                paseosRepository.deletePaseo(paseo)
                Snackbar.make(v, "Paseo eliminado con éxito", Snackbar.LENGTH_SHORT).show()
                val action = PaseoProgramadoDetailDirections.actionPaseoProgramadoDetailToHome()
                findNavController().navigate(action)
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
}