package com.example.myapplication.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.entities.User
import com.example.myapplication.entities.UserSession
import com.example.myapplication.helper.GeocodingHelper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng

class HomeDuenio : Fragment() {

    lateinit var v: View
    lateinit var text: TextView
    lateinit var btnPedidos: Button
    lateinit var btnPerfilPet: Button
    lateinit var btnHistorial: Button
    lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        v = inflater.inflate(R.layout.fragment_home_duenio, container, false)
        btnPedidos = v.findViewById(R.id.btnPaseadores)
        text = v.findViewById(R.id.welcomeTextDuenio)
        btnHistorial = v.findViewById(R.id.btnHistorial)
        btnPerfilPet = v.findViewById(R.id.btnPerfilPet)


        return v
    }

    @SuppressLint("MissingPermission")
    override fun onStart() {
        super.onStart()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        val user = UserSession.user as User
        text.text= "Tu Ubicacion: ${user.direccion}"

        text.setOnClickListener {
            if(user.direccion.isNullOrEmpty()) {
                fusedLocationClient.lastLocation.addOnSuccessListener {
                    if(it != null) {
                        val direccion = GeocodingHelper(requireContext()).getAddressFromLatLng(LatLng(it.latitude, it.longitude))
                        showPopUp(user, direccion)
                    }
                }
            }
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

    fun showPopUp(user: User, direccion: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Confirma tu Direccion")
        val input = EditText(requireContext())
        input.setText(direccion)
        builder.setView(input)

        builder.setPositiveButton("Confirmar") { dialog, _ ->
            user.direccion = input.text.toString()
            dialog.dismiss()
        }
    }
}
