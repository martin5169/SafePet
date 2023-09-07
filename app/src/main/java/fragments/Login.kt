package fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.google.android.material.snackbar.Snackbar
import entities.UserRepository

class Login : Fragment() {

    lateinit var v: View
    lateinit var btnLogin: Button
    lateinit var btnRegister: Button
    lateinit var name: EditText
    lateinit var pass: EditText
    val userRepository = UserRepository.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_login, container, false)

        btnLogin = v.findViewById(R.id.btnNavigate2)
        btnRegister = v.findViewById(R.id.btnNavigate)
        name = v.findViewById(R.id.name)
        pass = v.findViewById(R.id.userPassword)

        return v
    }

    override fun onStart() {
        super.onStart()

        btnLogin.setOnClickListener {
            try {
                val enteredMail = name.text.toString()
                val enteredPass = pass.text.toString()

                userRepository.getUsers { userList ->
                    val user = userList.find { it.mail == enteredMail }
                    if (user != null) {
                        if (user.password == enteredPass) {
                            val action = LoginDirections.actionLoginToHome2(user)
                            findNavController().navigate(action)
                        } else {
                            Snackbar.make(v, "Contraseña incorrecta", Snackbar.LENGTH_SHORT).show()
                        }
                    } else {
                        Snackbar.make(v, "Usuario no encontrado", Snackbar.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Snackbar.make(v, "Ocurrió un error al iniciar sesión", Snackbar.LENGTH_SHORT).show()
            }
        }


        btnRegister.setOnClickListener {
            val action = LoginDirections.actionLoginToRegisterOptions2()
            findNavController().navigate(action)
        }
    }
}
