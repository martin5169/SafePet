package com.example.myapplication.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class PaseoProgramado(): Parcelable {

    var paseador: Paseador  = Paseador()
    var user: User = User()
    var fecha : String = ""
    var id : String=""
    var estado: EstadoEnum = EstadoEnum.PENDIENTE
    var calificacion : Int = 0
    var medioDePago: MedioDePagoEnum = MedioDePagoEnum.EFECTIVO

    constructor(paseador: Paseador, user: UserAbstract, fecha: String, estadoEnum: EstadoEnum,calificacion:Int) : this() {
        this.paseador = paseador
        this.user = user as User
        this.fecha = fecha
        this.estado = estadoEnum
        this.calificacion = calificacion
    }

    override fun toString(): String {
        return "Paseo(paseador=$paseador, user=$user)"
    }


}