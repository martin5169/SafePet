package com.example.myapplication.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class PaseoProgramado(): Parcelable {

    var paseador: Paseador  = Paseador()
    var user: User = User()
    var fecha : String = ""

    constructor(paseador: Paseador, user: UserAbstract, fecha: String) : this() {
        this.paseador = paseador
        this.user = user as User
        this.fecha = fecha
    }

    override fun toString(): String {
        return "Paseo(paseador=$paseador, user=$user)"
    }


}