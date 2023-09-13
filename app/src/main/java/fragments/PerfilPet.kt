package fragments

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.text.set
import com.example.myapplication.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import entities.Pet
import entities.User
import entities.UserRepository
import kotlin.math.log

class PerfilPet : Fragment() {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()

    lateinit var v: View
    lateinit var btnRegister: Button

    lateinit var inputPetName: EditText
    lateinit var inputPetWeight: EditText
    lateinit var inputPetAge: EditText
    lateinit var inputPetBreed: EditText
    lateinit var alert : TextView

    lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_perfil_pet, container, false)
        btnRegister = v.findViewById(R.id.btnPerfilPetRegister)

        inputPetName = v.findViewById(R.id.petName)
        inputPetWeight = v.findViewById(R.id.petWeight)
        inputPetAge = v.findViewById(R.id.petAge)
        inputPetBreed = v.findViewById(R.id.petBreed)

        alert = v.findViewById(R.id.Alert)

        return v
    }

    override fun onStart() {
        super.onStart()
        user = HomeArgs.fromBundle(requireArguments()).user
        val userRepository = UserRepository.getInstance()

        val petNameNotEmpty = user.getPetName().isNotEmpty()
        val petWeightNotEmpty = user.getPetWeight().isNotEmpty()
        val petAgeNotEmpty = user.getPetAge().isNotEmpty()
        val petBreedNotEmpty = user.getPetBreed().isNotEmpty()

        if (petNameNotEmpty && petWeightNotEmpty && petAgeNotEmpty && petBreedNotEmpty) {
            btnRegister.text = "Editar datos"
            inputPetName.setText(user.getPetName())
            inputPetWeight.setText(user.getPetWeight())
            inputPetAge.setText(user.getPetAge())
            inputPetBreed.setText(user.getPetBreed())

        } else {
            btnRegister.visibility = View.VISIBLE
            alert.text = "Aún no has registrado a tu mascota"
        }

        btnRegister.setOnClickListener {
            val enteredName = inputPetName.text.toString()
            val enteredWeight = inputPetWeight.text.toString()
            val enteredAge = inputPetAge.text.toString()
            val enteredBreed = inputPetBreed.text.toString()

            userRepository.updatePet(user.dni, enteredName, enteredWeight, enteredAge, enteredBreed)

            btnRegister.visibility = View.GONE
            Snackbar.make(v, "Datos registrados con éxito", Snackbar.LENGTH_SHORT).show()
        }
    }
}

