package fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.myapplication.R
import com.google.android.material.snackbar.Snackbar
import entities.User
import entities.UserRepository

class RegisterForm : Fragment() {

    lateinit var v: View
    lateinit var btnRegister: Button
    lateinit var name: EditText
    lateinit var lastName: EditText
    lateinit var dni: EditText
    lateinit var mail: EditText
    lateinit var pass: EditText
    lateinit var pass2: EditText
    lateinit var userRepository: UserRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_register_form, container, false)

        btnRegister = v.findViewById(R.id.btnRegister)
        name = v.findViewById(R.id.name)
        lastName = v.findViewById(R.id.lastName)
        dni = v.findViewById(R.id.dni)
        mail = v.findViewById(R.id.mail)
        pass = v.findViewById(R.id.pass)
        pass2 = v.findViewById(R.id.pass2)


        userRepository = UserRepository.getInstance()

        return v
    }

    override fun onStart() {
        super.onStart()

        btnRegister.setOnClickListener {
            val enteredName = name.text.toString()
            val enteredLastName = lastName.text.toString()
            val enteredDni = dni.text.toString()
            val enteredMail = mail.text.toString()
            val enteredPass = pass.text.toString()
            val enteredPass2 = pass2.text.toString()

            if(enteredPass==enteredPass2){
                val newUser = User(enteredName, enteredLastName, enteredPass, enteredDni, enteredMail)

                userRepository.addUser(newUser)


                Snackbar.make(v, "Usuario registrado con éxito", Snackbar.LENGTH_SHORT).show()

            }

            else
            {
                Snackbar.make(v, "Las contraseñas no coinciden", Snackbar.LENGTH_SHORT).show()
            }


        }
    }
}


