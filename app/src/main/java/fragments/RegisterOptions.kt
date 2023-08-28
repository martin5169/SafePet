package fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import com.example.myapplication.R

class RegisterOptions : Fragment() {


    private lateinit var viewModel: RegisterOptionsViewModel
    lateinit var v: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_register_options, container, false)

        return v
    }

    override fun onStart() {
        super.onStart()

    }
}