package entities

import android.os.Parcelable
import android.telecom.TelecomManager
import kotlinx.parcelize.Parcelize

@Parcelize
class User(): Parcelable {


    var name: String = ""
    var lastName: String = ""
    var password: String = ""
    var dni: String = ""
    var mail: String = ""
    lateinit var mascota: Pet
    constructor(name: String, lastName: String, password: String, dni: String, mail:String) : this() {
        this.name = name
        this.password = password
        this.lastName = lastName
        this.dni=dni
        this.mail=mail
        this.mascota = Pet()

    }

    fun getPetName(): String {
        return this.mascota.nombre
    }
    fun getPetWeight(): String {
        return this.mascota.peso
    }

    fun getPetAge(): String {
        return this.mascota.edad
    }
    fun getPetBreed(): String {
        return this.mascota.raza
    }

    fun setPetName(nombreIngresado :String ){
        this.mascota.nombre=nombreIngresado
    }
    fun setPetWeight(pesoIngresado :String ){
        this.mascota.peso=pesoIngresado
    }

    fun setPetAge(edadIngresada :String ){
        this.mascota.edad=edadIngresada
    }
    fun setPetBreed(razaIngresada :String ){
        this.mascota.raza=razaIngresada
    }


}