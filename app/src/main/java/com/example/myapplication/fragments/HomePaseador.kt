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
import com.google.android.material.snackbar.Snackbar

class HomePaseador : Fragment() {

    lateinit var v: View
    lateinit var text: TextView
    lateinit var btnHistorial: Button
    lateinit var btnPerfilPaseador : Button
    lateinit var btnMediosCobro: Button
    lateinit var btnPasearAhora: Button
    lateinit var viewModel: HomePaseadorViewModel
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
            showConfirmationDialog(user.dni)
        }

    }

    private fun showConfirmationDialog(dni: String) {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle("Confirmación")
        val message = if((UserSession.user as Paseador).estaPaseando) {
            "¿Estás seguro de desactivar tu ubicación?"
        }else {
            "¿Estás seguro de hacer visible tu ubicación?"
        }
        builder.setMessage(message)
        builder.setPositiveButton("Sí") { _, _ ->
            paseadorRepository.updateEstaPaseando(dni, !(UserSession.user as Paseador).estaPaseando)
            (UserSession.user as Paseador).estaPaseando = !(UserSession.user as Paseador).estaPaseando
            viewModel.createLocationCallback(UserSession.user.dni)
            viewModel.startLocationUpdates()
            Snackbar.make(v, "Ubicación activada", Snackbar.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }

        builder.setNegativeButton("No") { _, _ ->
            // Usuario canceló, no se realiza la actualización
        }

        val dialog = builder.create()
        dialog.show()
    }

}