package com.example.myapplication.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.R

class MediosDeCobroPaseador : Fragment() {

    companion object {
        fun newInstance() = MediosDeCobroPaseador()
    }

    private lateinit var viewModel: MediosDeCobroPaseadorViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_medios_de_cobro_paseador, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MediosDeCobroPaseadorViewModel::class.java)
        // TODO: Use the ViewModel
    }

}