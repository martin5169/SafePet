package fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.myapplication.R

class Perfil : Fragment() {

    lateinit var v: View
    lateinit var name: TextView
    lateinit var lastName: TextView
    lateinit var mail: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_perfil
            , container, false)

        name = v.findViewById(R.id.name)
        lastName = v.findViewById(R.id.lastName)
        mail = v.findViewById(R.id.mail)


        return v
    }

    override fun onStart() {
        super.onStart()
        val user = PerfilArgs.fromBundle(requireArguments()).user
        name.text= "Nombre: ${user.name}"
        lastName.text = "Apellido: ${user.lastName}"
        mail.text= "Email: ${user.mail}"


    }



}