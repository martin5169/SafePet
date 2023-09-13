package entities

import android.os.Parcelable
import android.widget.EditText
import kotlinx.parcelize.Parcelize

@Parcelize
class Pet(): Parcelable {

    var nombre: String = ""
    var peso: String = ""
    var edad: String = ""
    var raza: String = ""

}

