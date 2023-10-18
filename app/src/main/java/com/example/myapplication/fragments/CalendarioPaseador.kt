package com.example.myapplication.fragments

import android.app.AlertDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CalendarView
import android.widget.Spinner
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.R.id.calendarPaseador
import com.example.myapplication.entities.EstadoEnum
import com.example.myapplication.entities.PaseoProgramado
import com.example.myapplication.entities.User
import com.example.myapplication.entities.UserAbstract
import com.example.myapplication.entities.UserSession.user
import com.example.myapplication.repository.PaseoProgramadoRepository
import com.google.android.material.snackbar.Snackbar

class CalendarioPaseador : Fragment() {

    lateinit var v : View
    lateinit var calendar : CalendarView
    lateinit var selectedDate: TextView
    lateinit var btnConfirm : Button
    lateinit var spinner : Spinner
    lateinit var adaptador: ArrayAdapter<String>
    val programadoRepository = PaseoProgramadoRepository.getInstance()
    val horarios = mutableListOf("10:00hs", "11:00hs", "12:00hs")
    private val originalHorarios = mutableListOf("10:00hs", "11:00hs", "12:00hs")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_calendario_paseador, container, false)
        calendar = v.findViewById(calendarPaseador)
        selectedDate = v.findViewById(R.id.selectedDate)
        btnConfirm = v.findViewById(R.id.btnConfirm)

        spinner = v.findViewById(R.id.listaHorarios)
        adaptador = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, horarios)
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adaptador

        return v
    }

    override fun onStart() {
        super.onStart()
        user = user as User

        val currentDateCalendar = Calendar.getInstance()
        val maxSelectableDateCalendar = Calendar.getInstance()

        maxSelectableDateCalendar.add(Calendar.DAY_OF_MONTH, 7)
        val maxSelectableDateInMillis = maxSelectableDateCalendar.timeInMillis

        calendar.minDate = currentDateCalendar.timeInMillis
        calendar.maxDate = maxSelectableDateInMillis

        calendar.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val selectedDateCalendar =
                "$dayOfMonth/${month + 1}/$year" // Suma 1 al mes ya que los meses comienzan en 0
            selectedDate.text = "$selectedDateCalendar"

            programadoRepository.getPaseos { paseos ->
                val fechasBuscadas = paseos.filter { it.fecha.substring(0, it.fecha.length - 7).trim() == selectedDateCalendar}
                val fechas = fechasBuscadas.map { it.fecha.substring(it.fecha.length - 7) }
                val fechasAgrupadas = fechas.groupingBy { it }.eachCount()
                val fechasRepetidas5Veces = fechasAgrupadas.filter { it.value == 5 }.keys.toList()
                if (fechasRepetidas5Veces.isNotEmpty()) {
                    val fechasFiltradas = horarios.filter { it !in fechasRepetidas5Veces }
                    val adaptadorActual = spinner.adapter as ArrayAdapter<String>
                    adaptadorActual.clear()
                    adaptadorActual.addAll(fechasFiltradas)
                    adaptadorActual.notifyDataSetChanged()
                } else {
                    Log.d("ASD", "isEmpty")
                    val adaptadorActual = spinner.adapter as ArrayAdapter<String>
                    adaptadorActual.clear()
                    adaptadorActual.addAll(originalHorarios)
                    adaptadorActual.notifyDataSetChanged()
                }
            }

        }

        btnConfirm.setOnClickListener {
            val selectedDateString = selectedDate.text.toString()
            val selectedTime = spinner.selectedItem.toString()
            if (selectedDateString.isNotEmpty() && selectedTime.isNotEmpty()) {
                val dateTimeString = "$selectedDateString $selectedTime"
                showConfirmationDialog(user, dateTimeString)

            } else {
                Snackbar.make(v, "Debe seleccionar una fecha válida", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun showConfirmationDialog(user: UserAbstract, dateTimeString: String) {
        val builder = AlertDialog.Builder(requireContext())
        val paseador = CalendarioPaseadorArgs.fromBundle(requireArguments()).Paseador
        val paseoProgramadoRepository = PaseoProgramadoRepository.getInstance()
        builder.setTitle("Confirmación")
        builder.setMessage("¿Confirma la fecha y hora seleccionadas?")
        builder.setPositiveButton("Sí") { _, _ ->
            // USUARIO CONFIRMA, HACER VALIDACIÓN DE FECHA DISPONIBLE
            val paseoProgramado = PaseoProgramado(paseador, user, dateTimeString, EstadoEnum.NO_ACTIVO,0)
            paseoProgramadoRepository.addPaseo(paseoProgramado)
            Snackbar.make(v, "Fecha y hora asignadas con éxito", Snackbar.LENGTH_SHORT).show()
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

