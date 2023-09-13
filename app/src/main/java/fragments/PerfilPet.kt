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

    lateinit var petName: EditText
    lateinit var weight: EditText
    lateinit var age: EditText
    lateinit var breed: EditText
    lateinit var alert: TextView

    lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_perfil_pet, container, false)
        btnRegister = v.findViewById(R.id.btnPerfilPetRegister)
        petName = v.findViewById(R.id.petName)
        weight = v.findViewById(R.id.petWeight)
        age = v.findViewById(R.id.petAge)
        breed = v.findViewById(R.id.petBreed)
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
            petName.setText(user.getPetName())
            weight.setText(user.getPetWeight())
            age.setText(user.getPetAge())
            breed.setText(user.getPetBreed())

        } else {
            btnRegister.visibility = View.VISIBLE
            alert.text = "Aún no has registrado a tu mascota"
        }

        btnRegister.setOnClickListener {
            val enteredName = petName.text.toString()
            val enteredWeight = weight.text.toString()
            val enteredAge = age.text.toString()
            val enteredBreed = breed.text.toString()

            if (enteredName.isNotEmpty() && enteredWeight.isNotEmpty() && enteredAge.isNotEmpty() && enteredBreed.isNotEmpty()) {
                userRepository.updatePet(user.dni, enteredName, enteredWeight, enteredAge, enteredBreed)
                btnRegister.visibility = View.GONE
                Snackbar.make(v, "Datos registrados con éxito", Snackbar.LENGTH_SHORT).show()
            } else {
                Snackbar.make(v, "Todos los campos son requeridos", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}

