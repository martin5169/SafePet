package com.example.myapplication.fragments

import android.app.AlertDialog
import android.icu.util.Calendar
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.R.id.calendarPaseador
import com.example.myapplication.entities.User
import com.example.myapplication.entities.UserAbstract
import com.example.myapplication.entities.UserSession
import com.example.myapplication.entities.UserSession.user
import com.example.myapplication.repository.UserRepository
import com.google.android.material.snackbar.Snackbar

class CalendarioPaseador : Fragment() {

    lateinit var v : View
    lateinit var calendar : CalendarView
    lateinit var selectedDate: TextView
    lateinit var btnConfirm : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_calendario_paseador, container, false)

        calendar = v.findViewById(calendarPaseador)
        selectedDate = v.findViewById(R.id.selectedDate)
        btnConfirm = v.findViewById(R.id.btnConfirm)


        return v
    }

    override fun onStart() {
        super.onStart()
        user = UserSession.user as User

        val currentDateCalendar = Calendar.getInstance()
        val maxSelectableDateCalendar = Calendar.getInstance()

        maxSelectableDateCalendar.add(Calendar.DAY_OF_MONTH, 7)


        val maxSelectableDateInMillis = maxSelectableDateCalendar.timeInMillis

        calendar.minDate = currentDateCalendar.timeInMillis
        calendar.maxDate = maxSelectableDateInMillis

        calendar.setOnDateChangeListener { view, year, month, dayOfMonth ->


                val selectedDateCalendar =
                    "$dayOfMonth/${month + 1}/$year" // Suma 1 al mes ya que los meses comienzan en 0

                selectedDate.text = selectedDateCalendar
            }

        btnConfirm.setOnClickListener {
            val updateSelectedDate = selectedDate.text.toString()
            if (updateSelectedDate.isNotEmpty()) {
                showConfirmationDialog(user, updateSelectedDate)
            } else {
                Snackbar.make(v, "Todos los campos son requeridos", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun showConfirmationDialog(user: UserAbstract, selectedDate: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Confirmación")
        builder.setMessage("¿Confirma la fecha seleccionada?" )
        builder.setPositiveButton("Sí") { _, _ ->
            // USUARIO CONFIRMA, HACER VALIDACION DE FECHA DISPONIBLE
            //TODO
                       Snackbar.make(v, "Fecha asignada con éxito", Snackbar.LENGTH_SHORT).show()
            val action = CalendarioPaseadorDirections.actionCalendarioPaseadorToPaseadoresList()
            findNavController().navigate(action)
        }

        builder.setNegativeButton("No") { _, _ ->
            // Usuario canceló, no se realiza la actualización
        }

        val dialog = builder.create()
        dialog.show()
    }

}

