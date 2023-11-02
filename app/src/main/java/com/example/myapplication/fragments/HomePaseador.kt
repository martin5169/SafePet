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
import com.example.myapplication.entities.EstadoEnum
import com.example.myapplication.entities.Paseador
import com.example.myapplication.entities.UserSession
import com.example.myapplication.repository.PaseadorRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar

class HomePaseador : Fragment() {

    lateinit var v: View
    lateinit var text: TextView
    lateinit var btnHistorial: Button
    lateinit var btnPerfilPaseador : Button
    lateinit var btnMediosCobro: Button
    lateinit var btnPasearAhora: Button
    lateinit var viewModel: HomePaseadorViewModel
    lateinit var location: FusedLocationProviderClient
    val paseadorRepository = PaseadorRepository.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        v = inflater.inflate(R.layout.fragment_home_paseador, container, false)

        btnPerfilPaseador = v.findViewById(R.id.btnPerfilPaseador)
        btnPasearAhora = v.findViewById(R.id.mapaPaseador2)
        btnHistorial = v.findViewById(R.id.btnHistorialPaseador)
        text = v.findViewById(R.id.welcomeTextPaseador)
        viewModel = HomePaseadorViewModel()
        location = LocationServices.getFusedLocationProviderClient(requireContext())
        btnMediosCobro = v.findViewById(R.id.mediosDeCobroPaseador2)


        return v
    }
    override fun onStart() {
        super.onStart()

        val user = UserSession.user
        btnPasearAhora.text = if ( (user as Paseador).estaPaseando ) {
            "Dejar de Pasear"
        }else {
            "Pasear Ahora"
        }
        text.text= "Bienvenido, ${user.name}"

        btnMediosCobro.setOnClickListener {
            val action = HomePaseadorDirections.actionHomePaseadorToMediosDeCobroPaseador()
            findNavController().navigate(action)
        }

        btnPerfilPaseador.setOnClickListener {
            val action = HomePaseadorDirections.actionHomePaseadorToPerfilPaseador2()
            findNavController().navigate(action)
        }

        btnHistorial.setOnClickListener {
           val action = HomePaseadorDirections.actionHomePaseadorToPaseadorHistorial()
            findNavController().navigate(action)
        }

        btnPasearAhora.setOnClickListener {
            val user = UserSession.user as Paseador
            val newState = !user.estaPaseando // Invierto el estado actual
            showConfirmationDialog(user.dni, newState)
        }

    }

    private fun showConfirmationDialog(dni: String, newState: Boolean) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Confirmación")
        val message = if(newState) {
            "¿Estás seguro de hacer visible tu ubicación?"
        } else {
            "¿Estás seguro de desactivar tu ubicación?"
        }
        builder.setMessage(message)
        builder.setPositiveButton("Sí") { _, _ ->
            val user = UserSession.user as Paseador
            paseadorRepository.updateEstaPaseando(dni, newState)
            viewModel.createLocationCallback(UserSession.user.dni)
            viewModel.startLocationUpdates(location)
            user.estaPaseando = newState
            btnPasearAhora.text = if (newState) {
                "Dejar de Pasear"
            } else {
                "Pasear Ahora"
            }

            if(newState) {
                Snackbar.make(v, "Ubicación activada", Snackbar.LENGTH_SHORT).show()
            } else {
                Snackbar.make(v, "Ubicación desactivada", Snackbar.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("No") { _, _ ->
            // Usuario canceló, no se realiza la actualización
        }

        val dialog = builder.create()
        dialog.show()
    }

}