package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivityPaseador : AppCompatActivity() {
    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_paseador)
        navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView5) as NavHostFragment
        bottomNavView = findViewById(R.id.bottom_bar_paseador)
        NavigationUI.setupWithNavController(bottomNavView,navHostFragment.navController)
    }
}