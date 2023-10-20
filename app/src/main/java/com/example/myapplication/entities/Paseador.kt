package com.example.myapplication.entities

import android.os.Parcelable
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import kotlinx.parcelize.Parcelize
import kotlin.math.roundToInt

@Parcelize
class Paseador(): Parcelable, UserAbstract() {

    var location: Location = Location()
    var estaPaseando: Boolean = false
    var tarifa: Int = 0
    var puntuaciones: MutableList<Int> = mutableListOf()
    var promedioPuntuaciones : Int = 0

    constructor(name: String, lastName: String, password: String, dni: String, mail:String) : this() {
        this.name = name
        this.password = password
        this.lastName = lastName
        this.dni=dni
        this.mail=mail
        this.tarifa=0
        this.location = Location()
        this.estaPaseando = false
        this.promedioPuntuaciones = 0
    }

    fun calcularPromedioPuntuaciones(): Int {
        if (puntuaciones.isNotEmpty()) {
            val sum = puntuaciones.sum()
            promedioPuntuaciones = (sum / puntuaciones.size).toDouble().roundToInt()
        } else {
            promedioPuntuaciones = 0
        }

        return promedioPuntuaciones
    }

    fun agregarCalificacion(calificacion: Int) {
        puntuaciones.add(calificacion)
        Log.d(puntuaciones.toString(),"lista")
    }




}
