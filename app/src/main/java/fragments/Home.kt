package fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.myapplication.R

class Home : Fragment() {

    lateinit var v: View
    lateinit var text: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_home, container, false)
        text = v.findViewById(R.id.welcomeText)

        val userName = arguments?.getString("userName")

        if (!userName.isNullOrEmpty()) {
            text.text = "Bienvenido, $userName"
        } else {
            text.text = "Bienvenido"
        }

        return v
    }
}
