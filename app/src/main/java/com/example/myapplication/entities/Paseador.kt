package com.example.myapplication.entities

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.parcelize.Parcelize

@Parcelize
class Paseador(): Parcelable, UserAbstract() {

    var location: Location = Location()
    var estaPaseando: Boolean = false
    constructor(name: String, lastName: String, password: String, dni: String, mail:String) : this() {
        this.name = name
        this.password = password
        this.lastName = lastName
        this.dni=dni
        this.mail=mail
        this.location = Location()
        this.estaPaseando = false
    }


}
