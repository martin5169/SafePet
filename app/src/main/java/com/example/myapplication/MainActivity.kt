package com.example.myapplication

import android.app.Application
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import android.util.TypedValue
import android.widget.EditText
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        class MyApplication : Application() {
            override fun onCreate() {
                super.onCreate()
                FirebaseApp.initializeApp(this)
            }
        }

    }
}
