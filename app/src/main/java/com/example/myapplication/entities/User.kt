package com.example.myapplication.entities

import android.os.Parcelable
import android.telecom.TelecomManager
import kotlinx.parcelize.Parcelize

@Parcelize
class User(): Parcelable, UserAbstract() {


    lateinit var mascota: Pet
    constructor(name: String, lastName: String, password: String, dni: String, mail:String) : this() {
        this.name = name
        this.password = password
        this.lastName = lastName
        this.dni=dni
        this.mail=mail
        this.mascota = Pet()

    }




}