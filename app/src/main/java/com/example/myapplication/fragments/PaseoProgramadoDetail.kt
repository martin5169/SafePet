package com.example.myapplication.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.R

class PaseoProgramadoDetail : Fragment() {

    companion object {
        fun newInstance() = PaseoProgramadoDetail()
    }

    private lateinit var viewModel: PaseoProgramadoDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_paseo_programado_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PaseoProgramadoDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}