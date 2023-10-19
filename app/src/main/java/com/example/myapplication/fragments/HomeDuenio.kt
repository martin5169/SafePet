package com.example.myapplication.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.entities.Location
import com.example.myapplication.entities.User
import com.example.myapplication.entities.UserSession
import com.example.myapplication.helper.GeocodingHelper
import com.example.myapplication.repository.UserRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng

class HomeDuenio : Fragment() {

    lateinit var v: View
    lateinit var text: TextView
    lateinit var textDireccion: TextView
    lateinit var btnPedidos: Button
    lateinit var btnPerfilPet: Button
    lateinit var btnHistorial: Button
    lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var progressBar: ProgressBar
    lateinit var mainLayout: ConstraintLayout
    val userRepository = UserRepository.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        v = inflater.inflate(R.layout.fragment_home_duenio, container, false)
        btnPedidos = v.findViewById(R.id.btnPaseadores)
        text = v.findViewById(R.id.welcomeTextDuenio)
        btnHistorial = v.findViewById(R.id.btnHistorial)
        btnPerfilPet = v.findViewById(R.id.btnPerfilPet)
        textDireccion = v.findViewById(R.id.direccionText)
        progressBar = v.findViewById(R.id.progressBar)
        mainLayout = v.findViewById(R.id.frameLayout2)
        return v
    }

    @SuppressLint("MissingPermission")
    override fun onStart() {
        super.onStart()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        progressBar.visibility = View.GONE
        val user = UserSession.user as User
        text.text = "Bienvenido, ${user.name}"
        textDireccion.text = if (user.direccion.isNullOrEmpty()) {
            "Seleccionar Ubicacion"
        } else {
            "${user.direccion}"
        }
        textDireccion.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            mainLayout.alpha = 0.7f
            mainLayout.isEnabled = false
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener {
                    if (it != null) {
                        val direccion = GeocodingHelper(requireContext()).getAddressFromLatLng(
                            LatLng(
                                it.latitude,
                                it.longitude
                            )
                        )
                        showPopUp(user, direccion, it)
                    }
                }
        }
        if(user.direccion.isNullOrEmpty()) {
            textDireccion.performClick()
        }
        btnPedidos.setOnClickListener {
            val action = HomeDuenioDirections.actionHome2ToUserList()
            findNavController().navigate(action)
        }

        btnHistorial.setOnClickListener {
            val action = HomeDuenioDirections.actionHomeToUserHistorial()
            findNavController().navigate(action)
        }

        btnPerfilPet.setOnClickListener {
            val action = HomeDuenioDirections.actionHome2ToPerfilPet()
            findNavController().navigate(action)
        }

    }

    fun showPopUp(user: User, direccion: String, location: android.location.Location) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Confirma tu Direccion")
        val input = EditText(requireContext())
        input.setText(direccion)
        builder.setView(input)

        builder.setPositiveButton("Confirmar") { dialog, _ ->
            (UserSession.user as User).direccion = input.text.toString()
            userRepository.updateDireccionUser(user.dni, input.text.toString(), location)
            textDireccion.text = input.text.toString()
            dialog.dismiss()
        }
        val dialog = builder.create()
        progressBar.visibility = View.GONE
        mainLayout.isEnabled = true
        mainLayout.alpha = 1f
        dialog.show()
    }
}
