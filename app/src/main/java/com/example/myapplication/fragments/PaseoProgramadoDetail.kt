package com.example.myapplication.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.R
import com.example.myapplication.entities.UserAbstract
import com.example.myapplication.entities.UserSession
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class PaseoProgramadoDetail : Fragment() {

    lateinit var location: FusedLocationProviderClient
    lateinit var userSession: UserAbstract
    companion object {
        fun newInstance() = PaseoProgramadoDetail()
    }

    private lateinit var viewModel: PaseoProgramadoDetailViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userSession = UserSession.user
        location = LocationServices.getFusedLocationProviderClient(requireContext())
        return inflater.inflate(R.layout.fragment_paseo_programado_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PaseoProgramadoDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

    fun comenzarPaseo() {
        viewModel.createLocationCallback(userSession.dni)
        viewModel.startLocationUpdates()
    }

}