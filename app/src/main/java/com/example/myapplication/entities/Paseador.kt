package com.example.myapplication.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Paseador(): Parcelable, UserAbstract() {
    constructor(name: String, lastName: String, password: String, dni: String, mail:String) : this() {
        this.name = name
        this.password = password
        this.lastName = lastName
        this.dni=dni
        this.mail=mail
    }


}
