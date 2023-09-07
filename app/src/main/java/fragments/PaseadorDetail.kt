package fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.myapplication.R

class PaseadorDetail : Fragment() {


    lateinit var v : View
    lateinit var paseadorLastname : TextView
    lateinit var paseadorName : TextView
    lateinit var paseadorDni : TextView
    lateinit var paseadorMail : TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_paseador_detail, container, false)
        paseadorLastname = v.findViewById(R.id.paseadorLastName)
        paseadorName = v.findViewById(R.id.paseadorName)
        paseadorDni = v.findViewById(R.id.paseadorDni)
        paseadorMail = v.findViewById(R.id.paseadorMail)


        return v
    }

    override fun onStart() {
        super.onStart()
        val paseador = PaseadorDetailArgs.fromBundle(requireArguments()).paseador
        paseadorLastname.text= paseador.lastName
        paseadorName.text= paseador.name
        paseadorDni.text= paseador.dni
        paseadorMail.text= paseador.mail

    }

}