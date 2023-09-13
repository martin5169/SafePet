package fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R

class RegisterOptions : Fragment() {
   
    lateinit var v: View
    lateinit var btnRegisterDueño : Button
    lateinit var btnRegisterPaseador : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_register_options, container, false)
        btnRegisterDueño = v.findViewById(R.id.bntRegisterDueño)
        btnRegisterPaseador = v.findViewById(R.id.btnRegisterPaseador)
        
        return v
    }
    
    override fun onStart() {
        super.onStart()

        btnRegisterDueño.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("opcion", "dueño")
            findNavController().navigate(R.id.action_registerOptions_to_registerForm, bundle)
        }

        btnRegisterPaseador.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("opcion", "paseador")
            findNavController().navigate(R.id.action_registerOptions_to_registerForm, bundle)
        }
       



    }
}