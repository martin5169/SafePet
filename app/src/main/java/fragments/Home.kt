package fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R

class Home : Fragment() {

    lateinit var v: View
    lateinit var text: TextView
    lateinit var btnPedidos: Button
    lateinit var btnPerfilUser: Button
    lateinit var btnPerfilPet: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        v = inflater.inflate(R.layout.fragment_home, container, false)
        btnPedidos = v.findViewById(R.id.btnPaseadores)
        text = v.findViewById(R.id.welcomeText)
        btnPerfilUser = v.findViewById(R.id.btnPerfil)
        btnPerfilPet = v.findViewById(R.id.btnPerfilPet)

        return v
    }

    override fun onStart() {
        super.onStart()

        val user = HomeArgs.fromBundle(requireArguments()).user
        text.text= "Bienvenido, ${user.name}"

        btnPedidos.setOnClickListener {
            val action = HomeDirections.actionHome2ToUserList()
            findNavController().navigate(action)
        }

        btnPerfilUser.setOnClickListener {
            val action = HomeDirections.actionHome2ToPerfil(user)
            findNavController().navigate(action)
        }

        btnPerfilPet.setOnClickListener {
            val action = HomeDirections.actionHome2ToPerfilPet()
            findNavController().navigate(action)
        }

    }
}
