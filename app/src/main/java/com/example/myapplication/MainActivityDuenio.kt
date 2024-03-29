package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.myapplication.fragments.CalificarPaseo
import com.example.myapplication.fragments.PaseadorDetail
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivityDuenio : AppCompatActivity() {

    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_duenio)
        navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView3) as NavHostFragment
        bottomNavView = findViewById(R.id.bottom_bar_duenio)
        NavigationUI.setupWithNavController(bottomNavView,navHostFragment.navController)

    }




}
