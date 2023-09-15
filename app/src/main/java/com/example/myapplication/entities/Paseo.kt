package com.example.myapplication.entities

class Paseo {

    var paseador: Paseador  = Paseador()
    var user: User = User()

    constructor(paseador: Paseador, user: User){
        this.paseador = paseador
        this.user = user
    }

    constructor()

    override fun toString(): String {
        return "Paseo(paseador=$paseador, user=$user)"
    }


}